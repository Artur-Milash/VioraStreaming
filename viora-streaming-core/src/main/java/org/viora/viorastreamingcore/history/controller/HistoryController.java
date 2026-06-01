package org.viora.viorastreamingcore.history.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.viora.viorastreamingcore.configs.security.SecurityHelpers;
import org.viora.viorastreamingcore.content.dto.MovieSummary;
import org.viora.viorastreamingcore.history.dto.HistoryDto;
import org.viora.viorastreamingcore.history.service.GetHistoryUseCase;
import org.viora.viorastreamingcore.history.service.SaveHistoryUseCase;
import org.viora.viorastreamingcore.history.service.command.SaveHistoryCommand;
import java.util.List;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class HistoryController {

  private final GetHistoryUseCase getHistoryUseCase;
  private final SaveHistoryUseCase saveHistoryUseCase;

  @GetMapping
  public List<HistoryDto> getHistory() {
    return getHistoryUseCase.getHistory();
  }

  @GetMapping("/{movieId}")
  public HistoryDto getHistoryById(@PathVariable Long movieId) {
    return getHistoryUseCase.getHistoryById(movieId);
  }

  @PostMapping("/{movieId}")
  public ResponseEntity<?> saveToHistory(@PathVariable Long movieId) {
    saveHistoryUseCase.saveHistory(movieId);
    return ResponseEntity.ok().build();
  }

}
