package org.viora.viorastreamingcore.history.controller.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.viora.viorastreamingcore.history.controller.HistoryController;
import org.viora.viorastreamingcore.history.dto.HistoryDto;
import org.viora.viorastreamingcore.history.service.GetHistoryUseCase;
import org.viora.viorastreamingcore.content.dto.MovieSummary;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryControllerTest {

  @Mock
  private GetHistoryUseCase getHistoryUseCase;

  @InjectMocks
  private HistoryController historyController;

  @Test
  void whenGetHistory_thenReturnsListFromUseCase() {
    // given
    HistoryDto dto = new HistoryDto(
        MovieSummary.builder()
            .id(1L)
            .title("Interstellar")
            .poster("poster.jpg")
            .releaseDate(LocalDate.of(2014, 11, 7))
            .genres(Set.of())
            .rating(8.6f)
            .durationInMinutes(169L)
            .plot("Space exploration.")
            .build(),
        1700000000L
    );

    when(getHistoryUseCase.getHistory()).thenReturn(List.of(dto));

    // when
    List<HistoryDto> result = historyController.getHistory();

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).movie().title()).isEqualTo("Interstellar");
    assertThat(result.get(0).lastWatchedAt()).isEqualTo(1700000000L);

    verify(getHistoryUseCase).getHistory();
  }

  @Test
  void whenGetHistoryById_thenReturnsHistoryDto() {
    // given
    Long movieId = 10L;

    HistoryDto dto = new HistoryDto(
        MovieSummary.builder()
            .id(movieId)
            .title("Inception")
            .poster("poster2.jpg")
            .releaseDate(LocalDate.of(2010, 7, 16))
            .genres(Set.of())
            .rating(8.8f)
            .durationInMinutes(148L)
            .plot("Dream layers.")
            .build(),
        1700001234L
    );

    when(getHistoryUseCase.getHistoryById(movieId)).thenReturn(dto);

    // when
    HistoryDto result = historyController.getHistoryById(movieId);

    // then
    assertThat(result.movie().id()).isEqualTo(movieId);
    assertThat(result.movie().title()).isEqualTo("Inception");
    assertThat(result.lastWatchedAt()).isEqualTo(1700001234L);

    verify(getHistoryUseCase).getHistoryById(movieId);
  }

  @Test
  void givenMovieId_whenGetHistoryById_thenPassesCorrectArgument() {
    // given
    Long movieId = 99L;

    when(getHistoryUseCase.getHistoryById(anyLong())).thenReturn(
        new HistoryDto(
            MovieSummary.builder()
                .id(movieId)
                .title("Test")
                .poster(null)
                .releaseDate(null)
                .genres(Set.of())
                .rating(null)
                .durationInMinutes(null)
                .plot(null)
                .build(),
            123L
        )
    );

    // when
    historyController.getHistoryById(movieId);

    // then
    ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
    verify(getHistoryUseCase).getHistoryById(captor.capture());

    assertThat(captor.getValue()).isEqualTo(movieId);
  }
}
