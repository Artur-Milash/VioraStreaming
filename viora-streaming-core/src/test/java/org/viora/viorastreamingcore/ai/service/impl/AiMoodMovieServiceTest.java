package org.viora.viorastreamingcore.ai.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.viora.viorastreamingcore.ai.dto.MoodMovieSuggestion;
import org.viora.viorastreamingcore.ai.service.MoodMovieService;
import org.viora.viorastreamingcore.content.dto.MovieSummary;
import org.viora.viorastreamingcore.content.service.MovieService;
import org.viora.viorastreamingcore.configs.security.SecurityHelpers;
import org.viora.viorastreamingcore.history.model.History;
import org.viora.viorastreamingcore.history.repository.HistoryRepository;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiMoodMovieServiceTest {

  @Mock
  private ChatClient.Builder chatClientBuilder;

  @Mock
  private ChatClient chatClient;

  @Mock
  private ChatClientRequestSpec requestSpec;

  @Mock
  private CallResponseSpec responseSpec;

  @Mock
  private MovieService movieService;

  @Mock
  private HistoryRepository historyRepository;

  @Mock
  private SecurityHelpers securityHelpers;

  private MoodMovieService service;

  @BeforeEach
  void setUp() {
    service = new MoodMovieService(
        chatClientBuilder,
        movieService,
        historyRepository,
        securityHelpers,
        new ObjectMapper()
    );

    when(chatClientBuilder.build()).thenReturn(chatClient);

    when(chatClient.prompt()).thenReturn(requestSpec);
    when(requestSpec.system(anyString())).thenReturn(requestSpec);
    when(requestSpec.user(anyString())).thenReturn(requestSpec);
    when(requestSpec.call()).thenReturn(responseSpec);
  }

  @Test
  void givenValidAiSuggestions_whenGetMoviesByMood_thenReturnsSuggestions() {

    MovieSummary movie1 = mock(MovieSummary.class);
    MovieSummary movie2 = mock(MovieSummary.class);

    when(movie1.id()).thenReturn(1L);
    when(movie1.title()).thenReturn("Interstellar");
    when(movie1.plot()).thenReturn("Space exploration");
    when(movie1.genres()).thenReturn(Set.of());

    when(movie2.id()).thenReturn(2L);
    when(movie2.title()).thenReturn("Inception");
    when(movie2.plot()).thenReturn("Dreams");
    when(movie2.genres()).thenReturn(Set.of());

    when(movieService.getMovies(any(PageRequest.class)))
        .thenReturn(new PageImpl<>(List.of(movie1, movie2)));

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(10L);

    when(historyRepository.getHistoryByAccountId(10L))
        .thenReturn(List.of());

    List<?> aiSuggestions = List.of(
        new MoodMovieService.AiSuggestion(1L, 95),
        new MoodMovieService.AiSuggestion(2L, 88)
    );

    when(responseSpec.responseEntity(any(ParameterizedTypeReference.class)))
        .thenReturn(new ResponseEntity<>(aiSuggestions, HttpStatus.OK));

    List<MoodMovieSuggestion> result =
        service.getMoviesByMood("happy");

    assertThat(result).hasSize(2);

    assertThat(result.get(0).movie()).isEqualTo(movie1);
    assertThat(result.get(0).matchLabel()).isEqualTo("AI Match 95%");

    assertThat(result.get(1).movie()).isEqualTo(movie2);
    assertThat(result.get(1).matchLabel()).isEqualTo("AI Match 88%");
  }

  @Test
  void givenNullAiResponse_whenGetMoviesByMood_thenReturnsEmptyList() {

    MovieSummary movie = mock(MovieSummary.class);

    when(movie.id()).thenReturn(1L);
    when(movie.title()).thenReturn("Movie");
    when(movie.plot()).thenReturn("");
    when(movie.genres()).thenReturn(Set.of());

    when(movieService.getMovies(any(PageRequest.class)))
        .thenReturn(new PageImpl<>(List.of(movie)));

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(1L);

    when(historyRepository.getHistoryByAccountId(anyLong()))
        .thenReturn(List.of());

    when(responseSpec.responseEntity(any(ParameterizedTypeReference.class)))
        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

    List<MoodMovieSuggestion> result =
        service.getMoviesByMood("sad");

    assertThat(result).isEmpty();
  }

  @Test
  void givenAiReturnsUnknownMovieIds_whenGetMoviesByMood_thenFiltersThemOut() {

    MovieSummary movie = mock(MovieSummary.class);

    when(movie.id()).thenReturn(1L);
    when(movie.title()).thenReturn("Known Movie");
    when(movie.plot()).thenReturn("");
    when(movie.genres()).thenReturn(Set.of());

    when(movieService.getMovies(any(PageRequest.class)))
        .thenReturn(new PageImpl<>(List.of(movie)));

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(1L);

    when(historyRepository.getHistoryByAccountId(anyLong()))
        .thenReturn(List.of());

    List<?> aiSuggestions = List.of(
        new MoodMovieService.AiSuggestion(1L, 90),
        new MoodMovieService.AiSuggestion(999L, 100)
    );

    when(responseSpec.responseEntity(any(ParameterizedTypeReference.class)))
        .thenReturn(new ResponseEntity<>(aiSuggestions, HttpStatus.OK));

    List<MoodMovieSuggestion> result =
        service.getMoviesByMood("exciting");

    assertThat(result).hasSize(1);
    assertThat(result.get(0).movie()).isEqualTo(movie);
  }

  @Test
  void givenWatchedMovies_whenGetMoviesByMood_thenPromptContainsWatchedTitles() {

    MovieSummary movie = mock(MovieSummary.class);

    when(movie.id()).thenReturn(1L);
    when(movie.title()).thenReturn("Interstellar");
    when(movie.plot()).thenReturn("");
    when(movie.genres()).thenReturn(Set.of());

    when(movieService.getMovies(any(PageRequest.class)))
        .thenReturn(new PageImpl<>(List.of(movie)));

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(5L);

    History history = mock(History.class);
    org.viora.viorastreamingcore.content.model.Movie watchedMovie =
        mock(org.viora.viorastreamingcore.content.model.Movie.class);

    when(watchedMovie.getId()).thenReturn(1L);
    when(history.getMovie()).thenReturn(watchedMovie);

    when(historyRepository.getHistoryByAccountId(5L))
        .thenReturn(List.of(history));

    when(responseSpec.responseEntity(any(ParameterizedTypeReference.class)))
        .thenReturn(new ResponseEntity<>(List.of(), HttpStatus.OK));

    service.getMoviesByMood("inspiring");

    ArgumentCaptor<String> promptCaptor =
        ArgumentCaptor.forClass(String.class);

    verify(requestSpec).system(promptCaptor.capture());

    assertThat(promptCaptor.getValue())
        .contains("Interstellar");
  }

  @Test
  void givenNoWatchHistory_whenGetMoviesByMood_thenPromptContainsNone() {

    MovieSummary movie = mock(MovieSummary.class);

    when(movie.id()).thenReturn(1L);
    when(movie.title()).thenReturn("Movie");
    when(movie.plot()).thenReturn("");
    when(movie.genres()).thenReturn(Set.of());

    when(movieService.getMovies(any(PageRequest.class)))
        .thenReturn(new PageImpl<>(List.of(movie)));

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(1L);

    when(historyRepository.getHistoryByAccountId(1L))
        .thenReturn(List.of());

    when(responseSpec.responseEntity(any(ParameterizedTypeReference.class)))
        .thenReturn(new ResponseEntity<>(List.of(), HttpStatus.OK));

    service.getMoviesByMood("relaxed");

    ArgumentCaptor<String> promptCaptor =
        ArgumentCaptor.forClass(String.class);

    verify(requestSpec).system(promptCaptor.capture());

    assertThat(promptCaptor.getValue())
        .contains("Movies the user has already watched")
        .contains("none");
  }
}