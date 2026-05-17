package org.viora.viorastreamingcore.ai.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.viora.viorastreamingcore.ai.model.MovieDiscussion;

public interface MovieDiscussionRepository extends JpaRepository<MovieDiscussion, Long> {
  Optional<MovieDiscussion> findByAccountIdAndMovieId(Long accountId, Long movieId);
}
