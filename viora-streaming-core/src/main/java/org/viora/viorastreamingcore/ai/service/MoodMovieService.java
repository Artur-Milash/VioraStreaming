package org.viora.viorastreamingcore.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.viora.viorastreamingcore.ai.dto.MoodMovieSuggestion;
import org.viora.viorastreamingcore.configs.security.SecurityHelpers;
import org.viora.viorastreamingcore.content.dto.MovieSummary;
import org.viora.viorastreamingcore.content.service.MovieService;
import org.viora.viorastreamingcore.history.repository.HistoryRepository;

@Service
public class MoodMovieService {

  private static final int CATALOG_SIZE = 50;
  private static final int PLOT_PREVIEW_LENGTH = 80;

  private final ChatClient chatClient;
  private final MovieService movieService;
  private final HistoryRepository historyRepository;
  private final SecurityHelpers securityHelpers;
  private final ObjectMapper objectMapper;

  record AiSuggestion(long movieId, int matchScore) {}

  public MoodMovieService(
      ChatClient.Builder chatClientBuilder,
      MovieService movieService,
      HistoryRepository historyRepository,
      SecurityHelpers securityHelpers,
      ObjectMapper objectMapper) {
    this.chatClient = chatClientBuilder.build();
    this.movieService = movieService;
    this.historyRepository = historyRepository;
    this.securityHelpers = securityHelpers;
    this.objectMapper = objectMapper;
  }

  public List<MoodMovieSuggestion> getMoviesByMood(String mood) {
    List<MovieSummary> catalog = movieService.getMovies(PageRequest.of(0, CATALOG_SIZE)).getContent();
    Map<Long, MovieSummary> catalogMap = catalog.stream()
        .collect(Collectors.toMap(MovieSummary::id, m -> m));

    Long accountId = securityHelpers.getCurrentlyAuthenticatedAccountId();
    Set<Long> watchedIds = historyRepository.getHistoryByAccountId(accountId).stream()
        .map(h -> h.getMovie().getId())
        .collect(Collectors.toSet());

    String watchedTitles = catalog.stream()
        .filter(m -> watchedIds.contains(m.id()))
        .map(MovieSummary::title)
        .collect(Collectors.joining(", "));
    if (watchedTitles.isBlank()) watchedTitles = "none";

    List<AiSuggestion> suggestions = chatClient.prompt()
        .system(buildSystemPrompt(mood, watchedTitles, buildCatalogSnippet(catalog)))
        .user("Recommend 5 movies for mood: " + mood)
        .call()
        .responseEntity(new ParameterizedTypeReference<List<AiSuggestion>>() {})
        .getEntity();

    if (suggestions == null) return List.of();

    return suggestions.stream()
        .filter(s -> catalogMap.containsKey(s.movieId()))
        .map(s -> new MoodMovieSuggestion(catalogMap.get(s.movieId()), "AI Match " + s.matchScore() + "%"))
        .collect(Collectors.toList());
  }

  private String buildSystemPrompt(String mood, String watchedTitles, String catalog) {
    return """
        You are a movie recommendation engine. Select exactly 5 movies from the catalog that best evoke the mood: "%s".

        Movies the user has already watched (prefer recommending unwatched ones): %s

        Movie catalog:
        %s

        Select 5 movies and score each by how strongly it matches the mood (0-100).
        """.formatted(mood, watchedTitles, catalog);
  }

  private String buildCatalogSnippet(List<MovieSummary> catalog) {
    try {
      List<Map<String, Object>> entries = catalog.stream()
          .map(m -> {
            String genres = m.genres().stream().map(g -> g.name()).collect(Collectors.joining("/"));
            String plot = m.plot() != null
                ? m.plot().substring(0, Math.min(PLOT_PREVIEW_LENGTH, m.plot().length()))
                : "";
            return Map.<String, Object>of("id", m.id(), "title", m.title(), "genres", genres, "plot", plot);
          })
          .toList();
      return objectMapper.writeValueAsString(entries);
    } catch (Exception e) {
      return "[]";
    }
  }
}
