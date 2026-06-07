package org.viora.viorastreamingcore.content.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "movies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String plot;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String poster;

  @Column(nullable = false)
  private String rated;

  @Column(nullable = false)
  private Float rating;

  private String videoUrl;

  @Column(nullable = false)
  private LocalDate releaseDate;

  @Column(nullable = false)
  private Long durationInMinutes;


  @ManyToMany
  @JoinTable(
      name = "movie_actors",
      joinColumns = @JoinColumn(name = "movie_id"),
      inverseJoinColumns = @JoinColumn(name = "actor_id")
  )
  private Set<Actor> actors;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "director_id")
  private Director director;

  @ManyToMany
  @JoinTable(
      name = "movie_genres",
      joinColumns = @JoinColumn(name = "movie_id"),
      inverseJoinColumns = @JoinColumn(name = "genre_id")
  )
  private Set<Genre> genres;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "writer_id")
  private Writer writer;

  @Column(nullable = false, unique = true)
  private String imdbId;
}
