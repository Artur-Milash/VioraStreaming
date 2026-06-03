package org.viora.viorastreamingcore.streaming.controller.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.viora.viorastreamingcore.streaming.controller.VideoStreamingController;
import org.viora.viorastreamingcore.streaming.service.GetMovieUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoStreamingControllerTest {

  @Mock
  private GetMovieUseCase getMovieUseCase;

  @Mock
  private Resource resource;

  @InjectMocks
  private VideoStreamingController videoStreamingController;

  @Test
  void givenMovieId_whenGetPlaylist_thenReturnsM3u8Resource() {
    // given
    String movieId = "movie-123";

    when(getMovieUseCase.getMoviePlayback(movieId)).thenReturn(resource);

    // when
    var response = videoStreamingController.getPlaylist(movieId);

    // then
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(response.getBody()).isEqualTo(resource);
    assertThat(response.getHeaders().getContentType())
        .isEqualTo(MediaType.parseMediaType("application/vnd.apple.mpegurl"));

    verify(getMovieUseCase).getMoviePlayback(movieId);
  }

  @Test
  void givenMovieIdAndSegment_whenGetSegment_thenReturnsTsResource() {
    // given
    String movieId = "movie-456";
    Long segment = 3L;

    when(getMovieUseCase.getMovieSegment(movieId, segment)).thenReturn(resource);

    // when
    var response = videoStreamingController.getSegment(movieId, segment);

    // then
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(response.getBody()).isEqualTo(resource);
    assertThat(response.getHeaders().getContentType())
        .isEqualTo(MediaType.parseMediaType("video/mp2t"));

    verify(getMovieUseCase).getMovieSegment(movieId, segment);
  }

  @Test
  void givenSegmentRequest_whenGetSegment_thenPassesCorrectArguments() {
    // given
    String movieId = "abc";
    Long segment = 10L;

    when(getMovieUseCase.getMovieSegment(anyString(), anyLong())).thenReturn(resource);

    // when
    videoStreamingController.getSegment(movieId, segment);

    // then
    ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Long> segmentCaptor = ArgumentCaptor.forClass(Long.class);

    verify(getMovieUseCase).getMovieSegment(idCaptor.capture(), segmentCaptor.capture());

    assertThat(idCaptor.getValue()).isEqualTo(movieId);
    assertThat(segmentCaptor.getValue()).isEqualTo(segment);
  }

  @Test
  void givenPlaylistRequest_whenGetPlaylist_thenPassesCorrectId() {
    // given
    String movieId = "playlist-999";

    when(getMovieUseCase.getMoviePlayback(anyString())).thenReturn(resource);

    // when
    videoStreamingController.getPlaylist(movieId);

    // then
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(getMovieUseCase).getMoviePlayback(captor.capture());

    assertThat(captor.getValue()).isEqualTo(movieId);
  }
}
