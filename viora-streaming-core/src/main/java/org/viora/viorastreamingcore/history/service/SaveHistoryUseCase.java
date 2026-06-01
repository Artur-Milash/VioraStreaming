package org.viora.viorastreamingcore.history.service;

import org.viora.viorastreamingcore.history.service.command.SaveHistoryCommand;

public interface SaveHistoryUseCase {

  void saveHistory(SaveHistoryCommand command);

  void saveHistory(Long movieId);

}
