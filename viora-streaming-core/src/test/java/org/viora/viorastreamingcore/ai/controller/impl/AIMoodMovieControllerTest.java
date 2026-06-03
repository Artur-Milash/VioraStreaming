package org.viora.viorastreamingcore.ai.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.viora.viorastreamingcore.ai.controller.MoodMovieController;
import org.viora.viorastreamingcore.ai.dto.MoodMovieSuggestion;
import org.viora.viorastreamingcore.ai.dto.MoodRequest;
import org.viora.viorastreamingcore.ai.service.MoodMovieService;
import org.viora.viorastreamingcore.content.dto.GenreDto;
import org.viora.viorastreamingcore.content.dto.MovieSummary;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AIMoodMovieControllerTest {

  private final MoodMovieService moodMovieService = mock(MoodMovieService.class);

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final MockMvc mockMvc = MockMvcBuilders
      .standaloneSetup(new MoodMovieController(moodMovieService))
      .build();

  @Test
  void givenValidMood_whenGetMoodMovies_thenMovieSuggestionsReturned() throws Exception {
    // given
    String mood = "happy";

    GenreDto genre = new GenreDto(1L, "Sci-Fi");

    MovieSummary movie = MovieSummary.builder()
        .id(1L)
        .title("Interstellar")
        .poster("/poster.jpg")
        .releaseDate(LocalDate.of(2014, 11, 7))
        .genres(Set.of(genre))
        .rating(8.7f)
        .durationInMinutes(169L)
        .plot("Space exploration movie")
        .build();

    MoodMovieSuggestion suggestion =
        new MoodMovieSuggestion(movie, "Feel-good sci-fi");

    when(moodMovieService.getMoviesByMood(mood))
        .thenReturn(List.of(suggestion));

    String requestBody = """
                {
                  "mood": "happy"
                }
                """;

    // when & then
    mockMvc.perform(post("/api/v1/ai/mood-movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].matchLabel")
            .value("Feel-good sci-fi"))
        .andExpect(jsonPath("$[0].movie.id")
            .value(1))
        .andExpect(jsonPath("$[0].movie.title")
            .value("Interstellar"))
        .andExpect(jsonPath("$[0].movie.poster")
            .value("/poster.jpg"))
        .andExpect(jsonPath("$[0].movie.rating")
            .value(8.7))
        .andExpect(jsonPath("$[0].movie.durationInMinutes")
            .value(169));

    verify(moodMovieService).getMoviesByMood(mood);
    verifyNoMoreInteractions(moodMovieService);
  }

  @Test
  void givenBlankMood_whenGetMoodMovies_thenBadRequestReturned() throws Exception {
    // given
    String requestBody = """
                {
                  "mood": ""
                }
                """;

    // when & then
    mockMvc.perform(post("/api/v1/ai/mood-movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isBadRequest());

    verifyNoInteractions(moodMovieService);
  }

  @Test
  void givenMissingMood_whenGetMoodMovies_thenBadRequestReturned() throws Exception {
    // given
    String requestBody = """
                {
                }
                """;

    // when & then
    mockMvc.perform(post("/api/v1/ai/mood-movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isBadRequest());

    verifyNoInteractions(moodMovieService);
  }

  @Test
  void givenValidMood_whenGetMoodMovies_thenServiceCalledWithExactMood() throws Exception {
    // given
    String mood = "relaxed";

    when(moodMovieService.getMoviesByMood(mood))
        .thenReturn(List.of());

    MoodRequest request = new MoodRequest(mood);

    // when
    mockMvc.perform(post("/api/v1/ai/mood-movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    // then
    verify(moodMovieService).getMoviesByMood(mood);
  }

  @Test
  void givenNoMatchingMovies_whenGetMoodMovies_thenEmptyListReturned() throws Exception {
    // given
    when(moodMovieService.getMoviesByMood("sad"))
        .thenReturn(List.of());

    String requestBody = """
                {
                  "mood": "sad"
                }
                """;

    // when & then
    mockMvc.perform(post("/api/v1/ai/mood-movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(moodMovieService).getMoviesByMood("sad");
  }
}