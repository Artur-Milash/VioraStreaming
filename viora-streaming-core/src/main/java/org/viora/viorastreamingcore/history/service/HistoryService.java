package org.viora.viorastreamingcore.history.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.viora.viorastreamingcore.account.model.AccountModel;
import org.viora.viorastreamingcore.account.repository.AccountRepository;
import org.viora.viorastreamingcore.configs.security.SecurityHelpers;
import org.viora.viorastreamingcore.content.dto.MovieSummary;
import org.viora.viorastreamingcore.content.model.Movie;
import org.viora.viorastreamingcore.content.repository.MovieRepository;
import org.viora.viorastreamingcore.history.model.History;
import org.viora.viorastreamingcore.history.repository.HistoryRepository;
import org.viora.viorastreamingcore.history.service.command.SaveHistoryCommand;

@Service
@RequiredArgsConstructor
public class HistoryService implements SaveHistoryUseCase {

  private final HistoryRepository historyRepository;
  private final AccountRepository accountRepository;
  private final MovieRepository movieRepository;
  private final SecurityHelpers securityHelpers;

  @EventListener
  @Override
  public void saveHistory(SaveHistoryCommand command) {
    AccountModel account = accountRepository.getReferenceById(command.getAccountId());
    Movie movie = movieRepository.getReferenceById(command.getMovieId());

    History history = historyRepository
        .findByAccountIdAndMovieId(command.getAccountId(), command.getMovieId())
        .orElse(new History(null, account, movie, command.getSegment()));

    history.setLastWatchedAt(command.getSegment());

    historyRepository.save(history);
  }

  @Override
  public void saveHistory(Long movieId) {
    this.saveHistory(
        new SaveHistoryCommand(this, securityHelpers.getCurrentlyAuthenticatedAccountId(),
            movieId,
            0L));
  }
}
