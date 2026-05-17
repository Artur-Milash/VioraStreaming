package org.viora.viorastreamingcore.ai.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.viora.viorastreamingcore.account.model.AccountModel;
import org.viora.viorastreamingcore.content.model.Movie;

@Entity
@Table(name = "movie_discussion")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieDiscussion {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_discussion_seq")
  @SequenceGenerator(name = "movie_discussion_seq", sequenceName = "movie_discussion_seq", allocationSize = 50)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "account_id",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "fk_movie_discussion_account")
  )
  private AccountModel account;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "movie_id",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "fk_movie_discussion_movie")
  )
  private Movie movie;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Long createdAt;
}
