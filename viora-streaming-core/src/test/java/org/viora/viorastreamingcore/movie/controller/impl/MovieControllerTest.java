package org.viora.viorastreamingcore.movie.controller.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.viora.viorastreamingcore.content.controller.MovieController;
import org.viora.viorastreamingcore.content.dto.*;
import org.viora.viorastreamingcore.content.service.MovieService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

  @Mock
  private MovieService movieService;

  @InjectMocks
  private MovieController movieController;

  @Test
  void givenFilterAndPageable_whenSearchMovies_thenDelegatesToService() {
    // given
    MovieFilter filter = new MovieFilter(
        "matrix",
        Set.of(1L, 2L),
        8.5f,
        null,
        null
    );

    Pageable pageable = Pageable.ofSize(20);

    Page<MovieSummary> serviceResult = new PageImpl<>(List.of(
        MovieSummary.builder()
            .id(1L)
            .title("The Matrix")
            .poster("poster.jpg")
            .releaseDate(LocalDate.of(1999, 3, 31))
            .genres(Set.of())
            .rating(8.7f)
            .durationInMinutes(136L)
            .plot("A hacker discovers reality is simulated.")
            .build()
    ));

    when(movieService.searchMovies(filter, pageable)).thenReturn(serviceResult);

    // when
    Page<MovieSummary> result = movieController.searchMovies(filter, pageable);

    // then
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getContent().get(0).title()).isEqualTo("The Matrix");

    verify(movieService).searchMovies(filter, pageable);
  }

  @Test
  void givenPageable_whenGetMovies_thenDelegatesToService() {
    // given
    Pageable pageable = Pageable.ofSize(20);

    Page<MovieSummary> serviceResult = new PageImpl<>(List.of(
        MovieSummary.builder()
            .id(2L)
            .title("Inception")
            .poster("poster2.jpg")
            .releaseDate(LocalDate.of(2010, 7, 16))
            .genres(Set.of())
            .rating(8.8f)
            .durationInMinutes(148L)
            .plot("Dream within a dream.")
            .build()
    ));

    when(movieService.getMovies(pageable)).thenReturn(serviceResult);

    // when
    Page<MovieSummary> result = movieController.getMovies(pageable);

    // then
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getContent().get(0).title()).isEqualTo("Inception");

    verify(movieService).getMovies(pageable);
  }

  @Test
  void givenMovieId_whenGetMovieById_thenReturnsResponseEntity() {
    // given
    Long movieId = 10L;

    MovieDto dto = new MovieDto(
        movieId,
        "Interstellar",
        "plot",
        "synopsis",
        "poster.jpg",
        "PG-13",
        8.6f,
        "video.mp4",
        LocalDate.of(2014, 11, 7),
        169L,
        Set.of(),
        null,
        Set.of(),
        null,
        "imdb123"
    );

    when(movieService.getMovieById(movieId)).thenReturn(dto);

    // when
    var response = movieController.getMovieById(movieId);

    // then
    assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(response.getBody()).isEqualTo(dto);

    verify(movieService).getMovieById(movieId);
  }

  @Test
  void givenFilter_whenSearchMovies_thenCapturesFilterCorrectly() {
    // given
    MovieFilter filter = new MovieFilter(
        "batman",
        Set.of(3L),
        7.0f,
        null,
        null
    );

    Pageable pageable = Pageable.ofSize(20);

    when(movieService.searchMovies(any(), any()))
        .thenReturn(Page.empty());

    // when
    movieController.searchMovies(filter, pageable);

    // then
    ArgumentCaptor<MovieFilter> filterCaptor =
        ArgumentCaptor.forClass(MovieFilter.class);

    ArgumentCaptor<Pageable> pageableCaptor =
        ArgumentCaptor.forClass(Pageable.class);

    verify(movieService).searchMovies(filterCaptor.capture(), pageableCaptor.capture());

    assertThat(filterCaptor.getValue().search()).isEqualTo("batman");
    assertThat(pageableCaptor.getValue()).isEqualTo(pageable);
  }
}
