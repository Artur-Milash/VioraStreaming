package org.viora.viorastreamingcore.history.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.viora.viorastreamingcore.configs.security.SecurityHelpers;
import org.viora.viorastreamingcore.content.dto.MovieSummary;
import org.viora.viorastreamingcore.content.model.Movie;
import org.viora.viorastreamingcore.content.service.MovieService;
import org.viora.viorastreamingcore.history.dto.HistoryDto;
import org.viora.viorastreamingcore.history.model.History;
import org.viora.viorastreamingcore.history.repository.HistoryRepository;
import org.viora.viorastreamingcore.history.service.GetHistoryService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetHistoryServiceTest {

  @Mock
  private MovieService movieService;

  @Mock
  private SecurityHelpers securityHelpers;

  @Mock
  private HistoryRepository repository;

  @InjectMocks
  private GetHistoryService service;

  @Test
  void givenUserHistory_whenGetHistory_thenReturnsMappedDtos() {

    Long accountId = 1L;

    Movie movie1 = mock(Movie.class);
    Movie movie2 = mock(Movie.class);

    when(movie1.getId()).thenReturn(100L);
    when(movie2.getId()).thenReturn(200L);

    History history1 = mock(History.class);
    History history2 = mock(History.class);

    when(history1.getMovie()).thenReturn(movie1);
    when(history1.getLastWatchedAt()).thenReturn(1000L);

    when(history2.getMovie()).thenReturn(movie2);
    when(history2.getLastWatchedAt()).thenReturn(2000L);

    MovieSummary summary1 = mock(MovieSummary.class);
    MovieSummary summary2 = mock(MovieSummary.class);

    when(summary1.id()).thenReturn(100L);
    when(summary2.id()).thenReturn(200L);

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(accountId);

    when(repository.getHistoryByAccountId(accountId))
        .thenReturn(List.of(history1, history2));

    when(movieService.getMoviesByIds(Set.of(100L, 200L)))
        .thenReturn(List.of(summary1, summary2));

    List<HistoryDto> result = service.getHistory();

    assertThat(result).hasSize(2);

    assertThat(result.get(0).movie()).isEqualTo(summary1);
    assertThat(result.get(0).lastWatchedAt()).isEqualTo(1000L);

    assertThat(result.get(1).movie()).isEqualTo(summary2);
    assertThat(result.get(1).lastWatchedAt()).isEqualTo(2000L);
  }

  @Test
  void givenNoHistory_whenGetHistory_thenReturnsEmptyList() {

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(1L);

    when(repository.getHistoryByAccountId(1L))
        .thenReturn(List.of());

    when(movieService.getMoviesByIds(Set.of()))
        .thenReturn(List.of());

    List<HistoryDto> result = service.getHistory();

    assertThat(result).isEmpty();
  }

  @Test
  void givenHistoryExists_whenGetHistoryById_thenReturnsHistoryDto() {

    Long accountId = 1L;
    Long movieId = 100L;

    MovieSummary summary = mock(MovieSummary.class);

    History history = mock(History.class);

    when(history.getLastWatchedAt()).thenReturn(5000L);

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(accountId);

    when(movieService.getMovieSummaryById(movieId))
        .thenReturn(summary);

    when(repository.findByAccountIdAndMovieId(accountId, movieId))
        .thenReturn(Optional.of(history));

    HistoryDto result = service.getHistoryById(movieId);

    assertThat(result.movie()).isEqualTo(summary);
    assertThat(result.lastWatchedAt()).isEqualTo(5000L);
  }

  @Test
  void givenHistoryMissing_whenGetHistoryById_thenReturnsDtoWithZeroTimestamp() {

    Long accountId = 1L;
    Long movieId = 100L;

    MovieSummary summary = mock(MovieSummary.class);

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(accountId);

    when(movieService.getMovieSummaryById(movieId))
        .thenReturn(summary);

    when(repository.findByAccountIdAndMovieId(accountId, movieId))
        .thenReturn(Optional.empty());

    HistoryDto result = service.getHistoryById(movieId);

    assertThat(result.movie()).isEqualTo(summary);
    assertThat(result.lastWatchedAt()).isEqualTo(0L);
  }

  @Test
  void givenMovieId_whenGetHistoryById_thenLoadsMovieSummary() {

    Long accountId = 1L;
    Long movieId = 100L;

    MovieSummary summary = mock(MovieSummary.class);

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(accountId);

    when(movieService.getMovieSummaryById(movieId))
        .thenReturn(summary);

    when(repository.findByAccountIdAndMovieId(accountId, movieId))
        .thenReturn(Optional.empty());

    service.getHistoryById(movieId);

    verify(movieService).getMovieSummaryById(movieId);
  }
}