package org.viora.viorastreamingcore.content.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.viora.viorastreamingcore.content.dto.GenreDto;
import org.viora.viorastreamingcore.content.dto.MovieDto;
import org.viora.viorastreamingcore.content.dto.MovieFilter;
import org.viora.viorastreamingcore.content.dto.MovieSummary;
import org.viora.viorastreamingcore.content.exception.MovieNotFoundException;
import org.viora.viorastreamingcore.content.model.Movie;
import org.viora.viorastreamingcore.content.repository.MovieRepository;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

  private final MovieRepository movieRepository;
  private final ObjectMapper objectMapper;

  @Override
  public Page<MovieSummary> searchMovies(MovieFilter filter, Pageable pageable) {
    Specification<Movie> spec = MovieSpecification.buildSpecification(filter);
    return movieRepository.findAll(spec, pageable).map(this::mapToMovieSummary);
  }

  @Override
  public Page<MovieSummary> getMovies(Pageable pageable) {
    var movies = movieRepository.findAllWithGenres(pageable);
    return movies.map(this::mapToMovieSummary);
  }

  @Override
  public MovieDto getMovieById(Long id) {
    return movieRepository.findFullMovieById(id)
        .map(movie -> objectMapper.convertValue(movie, MovieDto.class))
        .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + id));
  }

  @Override
  public MovieSummary getMovieByImdbId(String imdbId) {
    return movieRepository.findMoviesByImdbId(imdbId)
        .map(this::mapToMovieSummary)
        .orElseThrow(
            () -> new MovieNotFoundException("Movie with imdbId: " + imdbId + " not found"));
  }

  @Override
  public List<MovieSummary> getMoviesByIds(Set<Long> movieIds) {
    return movieRepository.findMoviesByIdIn(movieIds)
        .stream()
        .map(this::mapToMovieSummary)
        .collect(Collectors.toList());
  }

  @Override
  public MovieSummary getMovieSummaryById(Long id) {
    return movieRepository.findById(id).map(this::mapToMovieSummary)
        .orElseThrow(() -> new MovieNotFoundException("Movie with id %s not found".formatted(id)));
  }

  private MovieSummary mapToMovieSummary(Movie m) {
    return MovieSummary.builder()
        .id(m.getId())
        .title(m.getTitle())
        .poster(m.getPoster())
        .releaseDate(m.getReleaseDate())
        .videoUrl(m.getVideoUrl())
        .genres(m.getGenres().stream().map(genre -> new GenreDto(genre.getId(), genre.getName()))
            .collect(Collectors.toSet()))
        .rating(m.getRating())
        .durationInMinutes(m.getDurationInMinutes())
        .plot(m.getPlot())
        .build();
  }
}
