package org.viora.viorastreamingcore.content.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.viora.viorastreamingcore.content.dto.MovieDto;
import org.viora.viorastreamingcore.content.dto.MovieFilter;
import org.viora.viorastreamingcore.content.dto.MovieSummary;
import org.viora.viorastreamingcore.content.exception.MovieNotFoundException;
import org.viora.viorastreamingcore.content.model.Genre;
import org.viora.viorastreamingcore.content.model.Movie;
import org.viora.viorastreamingcore.content.repository.MovieRepository;
import org.viora.viorastreamingcore.content.service.MovieServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

  @Mock
  private MovieRepository movieRepository;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private MovieServiceImpl service;

  // ----------------------------------------------------
  // searchMovies
  // ----------------------------------------------------

  @Test
  void givenFilter_whenSearchMovies_thenReturnsMappedSummaries() {

    Movie movie = buildMovie();

    when(movieRepository.findAll(
        ArgumentMatchers.<Specification<Movie>>any(),
        any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(movie)));

    Page<MovieSummary> result =
        service.searchMovies(
            mock(MovieFilter.class),
            PageRequest.of(0, 10));

    assertThat(result.getContent()).hasSize(1);

    MovieSummary summary = result.getContent().get(0);

    assertThat(summary.id()).isEqualTo(1L);
    assertThat(summary.title()).isEqualTo("Interstellar");
    assertThat(summary.genres()).hasSize(1);
  }

  // ----------------------------------------------------
  // getMovies
  // ----------------------------------------------------

  @Test
  void givenMoviesExist_whenGetMovies_thenReturnsMappedSummaries() {

    Movie movie = buildMovie();

    when(movieRepository.findAllWithGenres(any(PageRequest.class)))
        .thenReturn(new PageImpl<>(List.of(movie)));

    Page<MovieSummary> result =
        service.getMovies(PageRequest.of(0, 20));

    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getContent().get(0).title())
        .isEqualTo("Interstellar");
  }

  // ----------------------------------------------------
  // getMovieById
  // ----------------------------------------------------

  @Test
  void givenExistingMovie_whenGetMovieById_thenReturnsDto() {

    Movie movie = buildMovie();

    MovieDto dto = mock(MovieDto.class);

    when(movieRepository.findFullMovieById(1L))
        .thenReturn(Optional.of(movie));

    when(objectMapper.convertValue(movie, MovieDto.class))
        .thenReturn(dto);

    MovieDto result = service.getMovieById(1L);

    assertThat(result).isSameAs(dto);
  }

  @Test
  void givenMissingMovie_whenGetMovieById_thenThrowsException() {

    when(movieRepository.findFullMovieById(1L))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.getMovieById(1L))
        .isInstanceOf(MovieNotFoundException.class)
        .hasMessage("Movie not found with id: 1");
  }

  // ----------------------------------------------------
  // getMovieByImdbId
  // ----------------------------------------------------

  @Test
  void givenExistingImdbId_whenGetMovieByImdbId_thenReturnsSummary() {

    Movie movie = buildMovie();

    when(movieRepository.findMoviesByImdbId("tt0816692"))
        .thenReturn(Optional.of(movie));

    MovieSummary result =
        service.getMovieByImdbId("tt0816692");

    assertThat(result.title())
        .isEqualTo("Interstellar");
  }

  @Test
  void givenMissingImdbId_whenGetMovieByImdbId_thenThrowsException() {

    when(movieRepository.findMoviesByImdbId("missing"))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() ->
        service.getMovieByImdbId("missing"))
        .isInstanceOf(MovieNotFoundException.class)
        .hasMessage("Movie with imdbId: missing not found");
  }

  // ----------------------------------------------------
  // getMoviesByIds
  // ----------------------------------------------------

  @Test
  void givenMovieIds_whenGetMoviesByIds_thenReturnsMappedSummaries() {

    Movie movie1 = buildMovie();

    Movie movie2 = buildMovie();
    movie2.setId(2L);
    movie2.setTitle("Inception");

    when(movieRepository.findMoviesByIdIn(Set.of(1L, 2L)))
        .thenReturn(List.of(movie1, movie2));

    List<MovieSummary> result =
        service.getMoviesByIds(Set.of(1L, 2L));

    assertThat(result).hasSize(2);

    assertThat(result)
        .extracting(MovieSummary::title)
        .containsExactlyInAnyOrder(
            "Interstellar",
            "Inception");
  }

  // ----------------------------------------------------
  // getMovieSummaryById
  // ----------------------------------------------------

  @Test
  void givenExistingMovie_whenGetMovieSummaryById_thenReturnsSummary() {

    Movie movie = buildMovie();

    when(movieRepository.findById(1L))
        .thenReturn(Optional.of(movie));

    MovieSummary result =
        service.getMovieSummaryById(1L);

    assertThat(result.id()).isEqualTo(1L);
    assertThat(result.title()).isEqualTo("Interstellar");
  }

  @Test
  void givenMissingMovie_whenGetMovieSummaryById_thenThrowsException() {

    when(movieRepository.findById(1L))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() ->
        service.getMovieSummaryById(1L))
        .isInstanceOf(MovieNotFoundException.class)
        .hasMessage("Movie with id 1 not found");
  }

  // ----------------------------------------------------
  // helpers
  // ----------------------------------------------------

  private Movie buildMovie() {

    Genre genre = new Genre();
    genre.setId(10L);
    genre.setName("Sci-Fi");

    Movie movie = new Movie();
    movie.setId(1L);
    movie.setTitle("Interstellar");
    movie.setPoster("poster.jpg");
    movie.setPlot("Space exploration");
    movie.setRating(8.7f);
    movie.setDurationInMinutes(169L);
    movie.setReleaseDate(LocalDate.of(2014, 11, 7));
    movie.setGenres(Set.of(genre));

    return movie;
  }
}