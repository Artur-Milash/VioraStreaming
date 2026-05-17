package org.viora.viorastreamingcore.ai.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.viora.viorastreamingcore.ai.model.DiscussionMessage;

public interface DiscussionMessageRepository extends JpaRepository<DiscussionMessage, Long> {
  List<DiscussionMessage> findByDiscussionIdOrderByCreatedAtAsc(Long discussionId);
}
