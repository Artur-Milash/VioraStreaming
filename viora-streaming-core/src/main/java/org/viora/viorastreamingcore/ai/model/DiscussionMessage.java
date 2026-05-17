package org.viora.viorastreamingcore.ai.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Entity
@Table(name = "discussion_message")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiscussionMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discussion_message_seq")
  @SequenceGenerator(name = "discussion_message_seq", sequenceName = "discussion_message_seq", allocationSize = 50)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "discussion_id",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "fk_discussion_message_discussion")
  )
  private MovieDiscussion discussion;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private MessageRole role;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Long createdAt;
}
