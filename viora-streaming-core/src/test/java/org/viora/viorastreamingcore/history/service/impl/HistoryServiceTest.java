package org.viora.viorastreamingcore.history.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.viora.viorastreamingcore.account.model.AccountModel;
import org.viora.viorastreamingcore.account.repository.AccountRepository;
import org.viora.viorastreamingcore.content.model.Movie;
import org.viora.viorastreamingcore.content.repository.MovieRepository;
import org.viora.viorastreamingcore.history.model.History;
import org.viora.viorastreamingcore.history.repository.HistoryRepository;
import org.viora.viorastreamingcore.history.service.HistoryService;
import org.viora.viorastreamingcore.history.service.command.SaveHistoryCommand;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

  @Mock
  private HistoryRepository historyRepository;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private MovieRepository movieRepository;

  @InjectMocks
  private HistoryService historyService;

  @Test
  void givenExistingHistory_whenSaveHistory_thenUpdatesLastWatchedAt() {

    Long accountId = 1L;
    Long movieId = 10L;
    Long segment = 500L;

    SaveHistoryCommand command =
        new SaveHistoryCommand(this, accountId, movieId, segment);

    AccountModel account = new AccountModel();
    account.setId(accountId);

    Movie movie = new Movie();
    movie.setId(movieId);

    History existingHistory =
        new History(
            100L,
            account,
            movie,
            100L
        );

    when(accountRepository.getReferenceById(accountId))
        .thenReturn(account);

    when(movieRepository.getReferenceById(movieId))
        .thenReturn(movie);

    when(historyRepository.findByAccountIdAndMovieId(accountId, movieId))
        .thenReturn(Optional.of(existingHistory));

    historyService.saveHistory(command);

    ArgumentCaptor<History> captor =
        ArgumentCaptor.forClass(History.class);

    verify(historyRepository).save(captor.capture());

    History savedHistory = captor.getValue();

    assertThat(savedHistory.getId()).isEqualTo(100L);
    assertThat(savedHistory.getLastWatchedAt()).isEqualTo(segment);
  }

  @Test
  void givenMissingHistory_whenSaveHistory_thenCreatesNewHistory() {

    Long accountId = 1L;
    Long movieId = 10L;
    Long segment = 900L;

    SaveHistoryCommand command =
        new SaveHistoryCommand(this, accountId, movieId, segment);

    AccountModel account = new AccountModel();
    account.setId(accountId);

    Movie movie = new Movie();
    movie.setId(movieId);

    when(accountRepository.getReferenceById(accountId))
        .thenReturn(account);

    when(movieRepository.getReferenceById(movieId))
        .thenReturn(movie);

    when(historyRepository.findByAccountIdAndMovieId(accountId, movieId))
        .thenReturn(Optional.empty());

    historyService.saveHistory(command);

    ArgumentCaptor<History> captor =
        ArgumentCaptor.forClass(History.class);

    verify(historyRepository).save(captor.capture());

    History savedHistory = captor.getValue();

    assertThat(savedHistory.getId()).isNull();
    assertThat(savedHistory.getAccount()).isEqualTo(account);
    assertThat(savedHistory.getMovie()).isEqualTo(movie);
    assertThat(savedHistory.getLastWatchedAt()).isEqualTo(segment);
  }

  @Test
  void givenCommand_whenSaveHistory_thenLoadsAccountAndMovieReferences() {

    Long accountId = 1L;
    Long movieId = 10L;

    SaveHistoryCommand command =
        new SaveHistoryCommand(this, accountId, movieId, 100L);

    when(accountRepository.getReferenceById(accountId))
        .thenReturn(mock(AccountModel.class));

    when(movieRepository.getReferenceById(movieId))
        .thenReturn(mock(Movie.class));

    when(historyRepository.findByAccountIdAndMovieId(accountId, movieId))
        .thenReturn(Optional.empty());

    historyService.saveHistory(command);

    verify(accountRepository).getReferenceById(accountId);
    verify(movieRepository).getReferenceById(movieId);
  }

  @Test
  void givenExistingHistory_whenSaveHistory_thenRepositoryLookupUsesCorrectIds() {

    Long accountId = 7L;
    Long movieId = 99L;

    SaveHistoryCommand command =
        new SaveHistoryCommand(this, accountId, movieId, 250L);

    when(accountRepository.getReferenceById(accountId))
        .thenReturn(mock(AccountModel.class));

    when(movieRepository.getReferenceById(movieId))
        .thenReturn(mock(Movie.class));

    when(historyRepository.findByAccountIdAndMovieId(accountId, movieId))
        .thenReturn(Optional.empty());

    historyService.saveHistory(command);

    verify(historyRepository)
        .findByAccountIdAndMovieId(accountId, movieId);
  }
}