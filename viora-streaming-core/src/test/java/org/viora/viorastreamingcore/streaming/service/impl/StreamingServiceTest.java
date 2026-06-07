package org.viora.viorastreamingcore.streaming.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.viora.viorastreamingcore.configs.security.SecurityHelpers;
import org.viora.viorastreamingcore.content.dto.MovieSummary;
import org.viora.viorastreamingcore.content.service.MovieService;
import org.viora.viorastreamingcore.history.service.command.SaveHistoryCommand;
import org.viora.viorastreamingcore.streaming.repository.StreamingRepository;
import org.viora.viorastreamingcore.streaming.service.StreamingService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StreamingServiceTest {

  @Mock
  private StreamingRepository streamingRepository;

  @Mock
  private MovieService movieService;

  @Mock
  private ApplicationEventPublisher eventPublisher;

  @Mock
  private SecurityHelpers securityHelpers;

  @Mock
  private Resource resource;

  @InjectMocks
  private StreamingService streamingService;


  @Test
  void givenValidId_whenGetMoviePlayback_thenReturnsResource() {

    when(streamingRepository.getMoviePlayback("tt123"))
        .thenReturn(resource);

    Resource result = streamingService.getMoviePlayback("tt123");

    assertThat(result).isEqualTo(resource);
    verify(streamingRepository).getMoviePlayback("tt123");
  }

  @Test
  void givenNullId_whenGetMoviePlayback_thenThrowsException() {

    assertThrows(IllegalArgumentException.class,
        () -> streamingService.getMoviePlayback(null));

    verifyNoInteractions(streamingRepository);
  }


  @Test
  void givenValidInput_whenGetMovieSegment_thenReturnsResourceAndPublishesEvent() {

    String imdbId = "tt123";
    Long segmentId = 42L;

    MovieSummary movieSummary = mock(MovieSummary.class);

    when(movieSummary.id()).thenReturn(10L);

    when(movieService.getMovieByImdbId(imdbId))
        .thenReturn(movieSummary);

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(99L);

    when(streamingRepository.getMovieSegment(imdbId, segmentId))
        .thenReturn(resource);

    Resource result = streamingService.getMovieSegment(imdbId, segmentId);

    assertThat(result).isEqualTo(resource);

    verify(streamingRepository).getMovieSegment(imdbId, segmentId);
    verify(eventPublisher).publishEvent(any(SaveHistoryCommand.class));
  }

  @Test
  void givenNullId_whenGetMovieSegment_thenThrowsException() {

    assertThrows(IllegalArgumentException.class,
        () -> streamingService.getMovieSegment(null, 1L));

    verifyNoInteractions(streamingRepository);
    verifyNoInteractions(eventPublisher);
  }

  @Test
  void givenNullSegment_whenGetMovieSegment_thenThrowsException() {

    assertThrows(IllegalArgumentException.class,
        () -> streamingService.getMovieSegment("tt123", null));

    verifyNoInteractions(streamingRepository);
    verifyNoInteractions(eventPublisher);
  }


  @Test
  void givenValidInput_whenGetMovieSegment_thenPublishesCorrectEvent() {

    String imdbId = "tt999";
    Long segmentId = 55L;

    MovieSummary movieSummary = mock(MovieSummary.class);

    when(movieSummary.id()).thenReturn(777L);

    when(movieService.getMovieByImdbId(imdbId))
        .thenReturn(movieSummary);

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(123L);

    when(streamingRepository.getMovieSegment(imdbId, segmentId))
        .thenReturn(resource);

    streamingService.getMovieSegment(imdbId, segmentId);

    ArgumentCaptor<SaveHistoryCommand> captor =
        ArgumentCaptor.forClass(SaveHistoryCommand.class);

    verify(eventPublisher).publishEvent(captor.capture());

    SaveHistoryCommand event = captor.getValue();

    assertThat(event.getAccountId()).isEqualTo(123L);
    assertThat(event.getMovieId()).isEqualTo(777L);
    assertThat(event.getSegment()).isEqualTo(55L);
  }
}