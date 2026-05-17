package org.viora.viorastreamingcore.ai.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.viora.viorastreamingcore.ai.dto.MoodMovieSuggestion;
import org.viora.viorastreamingcore.ai.dto.MoodRequest;
import org.viora.viorastreamingcore.ai.service.MoodMovieService;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class MoodMovieController {

  private final MoodMovieService moodMovieService;

  @PostMapping("/mood-movies")
  public List<MoodMovieSuggestion> getMoodMovies(@Valid @RequestBody MoodRequest request) {
    return moodMovieService.getMoviesByMood(request.mood());
  }

}
