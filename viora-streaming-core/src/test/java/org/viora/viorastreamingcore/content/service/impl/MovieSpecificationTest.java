package org.viora.viorastreamingcore.content.service.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;
import org.viora.viorastreamingcore.content.dto.Duration;
import org.viora.viorastreamingcore.content.dto.MovieFilter;
import org.viora.viorastreamingcore.content.dto.ReleaseYear;
import org.viora.viorastreamingcore.content.model.Genre;
import org.viora.viorastreamingcore.content.model.Movie;
import org.viora.viorastreamingcore.content.service.MovieSpecification;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MovieSpecificationTest {

  @Test
  void givenNullTitle_whenHasTitle_thenReturnsNullPredicate() {

    Specification<Movie> spec =
        MovieSpecification.hasTitle(null);

    Root<Movie> root = mock(Root.class);
    CriteriaQuery<?> query = mock(CriteriaQuery.class);
    CriteriaBuilder cb = mock(CriteriaBuilder.class);

    assertThat(spec.toPredicate(root, query, cb))
        .isNull();
  }

  @Test
  void givenTitle_whenHasTitle_thenUsesLikePredicate() {

    Root<Movie> root = mock(Root.class);
    CriteriaQuery<?> query = mock(CriteriaQuery.class);
    CriteriaBuilder cb = mock(CriteriaBuilder.class);

    Path<String> titlePath = mock(Path.class);
    Expression<String> lowerExpr = mock(Expression.class);
    Predicate predicate = mock(Predicate.class);

    when(root.<String>get("title")).thenReturn(titlePath);
    when(cb.lower(titlePath)).thenReturn(lowerExpr);
    when(cb.like(lowerExpr, "%interstellar%"))
        .thenReturn(predicate);

    Predicate result =
        MovieSpecification.hasTitle("Interstellar")
            .toPredicate(root, query, cb);

    assertThat(result).isEqualTo(predicate);

    verify(cb).lower(titlePath);
    verify(cb).like(lowerExpr, "%interstellar%");
  }

  @Test
  void givenNullGenres_whenHasGenres_thenReturnsNull() {

    Predicate result =
        MovieSpecification.hasGenres(null)
            .toPredicate(
                mock(Root.class),
                mock(CriteriaQuery.class),
                mock(CriteriaBuilder.class));

    assertThat(result).isNull();
  }

  @Test
  void givenEmptyGenres_whenHasGenres_thenReturnsNull() {

    Predicate result =
        MovieSpecification.hasGenres(Set.of())
            .toPredicate(
                mock(Root.class),
                mock(CriteriaQuery.class),
                mock(CriteriaBuilder.class));

    assertThat(result).isNull();
  }

  @Test
  void givenGenres_whenHasGenres_thenDistinctAndJoinUsed() {

    Root<Movie> root = mock(Root.class);
    CriteriaQuery<?> query = mock(CriteriaQuery.class);
    CriteriaBuilder cb = mock(CriteriaBuilder.class);

    Join<Movie, Genre> join = mock(Join.class);
    Path<Object> idPath = mock(Path.class);
    Predicate predicate = mock(Predicate.class);

    when(root.<Movie, Genre>join("genres", jakarta.persistence.criteria.JoinType.INNER))
        .thenReturn(join);

    when(join.get("id")).thenReturn(idPath);

    when(idPath.in(Set.of(1L, 2L)))
        .thenReturn(predicate);

    Predicate result =
        MovieSpecification.hasGenres(Set.of(1L, 2L))
            .toPredicate(root, query, cb);

    assertThat(result).isEqualTo(predicate);

    verify(query).distinct(true);
  }

  @Test
  void givenNullRating_whenHasRating_thenReturnsNull() {

    Predicate result =
        MovieSpecification.hasRating(null)
            .toPredicate(
                mock(Root.class),
                mock(CriteriaQuery.class),
                mock(CriteriaBuilder.class));

    assertThat(result).isNull();
  }

  @Test
  void givenRating_whenHasRating_thenUsesGreaterThan() {

    Root<Movie> root = mock(Root.class);
    CriteriaQuery<?> query = mock(CriteriaQuery.class);
    CriteriaBuilder cb = mock(CriteriaBuilder.class);

    Path<Float> ratingPath = mock(Path.class);
    Predicate predicate = mock(Predicate.class);

    when(root.<Float>get("rating")).thenReturn(ratingPath);
    when(cb.greaterThan(ratingPath, 8.0f))
        .thenReturn(predicate);

    Predicate result =
        MovieSpecification.hasRating(8.0f)
            .toPredicate(root, query, cb);

    assertThat(result).isEqualTo(predicate);
  }

  @Test
  void givenNullReleaseYear_whenHasReleaseYear_thenReturnsNull() {

    Predicate result =
        MovieSpecification.hasReleaseYear(null)
            .toPredicate(
                mock(Root.class),
                mock(CriteriaQuery.class),
                mock(CriteriaBuilder.class));

    assertThat(result).isNull();
  }

  @Test
  void givenReleaseYear_whenHasReleaseYear_thenUsesBetween() {

    Root<Movie> root = mock(Root.class);
    CriteriaQuery<?> query = mock(CriteriaQuery.class);
    CriteriaBuilder cb = mock(CriteriaBuilder.class);

    Path<LocalDate> path = mock(Path.class);
    Predicate predicate = mock(Predicate.class);

    when(root.<LocalDate>get("releaseDate")).thenReturn(path);

    LocalDate from = LocalDate.of(2000, 1, 1);
    LocalDate to = LocalDate.of(2005, 12, 31);

    when(cb.between(path, from, to))
        .thenReturn(predicate);

    Predicate result =
        MovieSpecification.hasReleaseYear(
                new ReleaseYear(2000, 2005))
            .toPredicate(root, query, cb);

    assertThat(result).isEqualTo(predicate);
  }

  @Test
  void givenDurationWithNullFrom_whenHasDuration_thenDefaultsToZero() {

    Root<Movie> root = mock(Root.class);
    CriteriaQuery<?> query = mock(CriteriaQuery.class);
    CriteriaBuilder cb = mock(CriteriaBuilder.class);

    Path<Integer> path = mock(Path.class);
    Predicate predicate = mock(Predicate.class);

    when(root.<Integer>get("durationInMinutes")).thenReturn(path);

    when(cb.between(path, 0, 120))
        .thenReturn(predicate);

    Predicate result =
        MovieSpecification.hasDurationBetween(
                new Duration(null, 120))
            .toPredicate(root, query, cb);

    assertThat(result).isEqualTo(predicate);
  }

  @Test
  void givenDurationWithNullTo_whenHasDuration_thenDefaultsTo10000() {

    Root<Movie> root = mock(Root.class);
    CriteriaQuery<?> query = mock(CriteriaQuery.class);
    CriteriaBuilder cb = mock(CriteriaBuilder.class);

    Path<Integer> path = mock(Path.class);
    Predicate predicate = mock(Predicate.class);

    when(root.<Integer>get("durationInMinutes"))
        .thenReturn(path);

    when(cb.between(path, 90, 10000))
        .thenReturn(predicate);

    Predicate result =
        MovieSpecification.hasDurationBetween(
                new Duration(90, null))
            .toPredicate(root, query, cb);

    assertThat(result).isEqualTo(predicate);
  }

  @Test
  void givenFilter_whenBuildSpecification_thenReturnsSpecification() {

    MovieFilter filter = new MovieFilter(
        "Interstellar",
        Set.of(1L),
        8.0f,
        new ReleaseYear(2000, 2020),
        new Duration(90, 180)
    );

    Specification<Movie> spec =
        MovieSpecification.buildSpecification(filter);

    assertThat(spec).isNotNull();
  }
}