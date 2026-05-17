package org.viora.viorastreamingcore.ai.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.viora.viorastreamingcore.account.repository.AccountRepository;
import org.viora.viorastreamingcore.ai.dto.CreateDiscussionRequest;
import org.viora.viorastreamingcore.ai.dto.DiscussionResponse;
import org.viora.viorastreamingcore.ai.dto.MessageResponse;
import org.viora.viorastreamingcore.ai.dto.SendMessageRequest;
import org.viora.viorastreamingcore.ai.model.DiscussionMessage;
import org.viora.viorastreamingcore.ai.model.MessageRole;
import org.viora.viorastreamingcore.ai.model.MovieDiscussion;
import org.viora.viorastreamingcore.ai.repository.DiscussionMessageRepository;
import org.viora.viorastreamingcore.ai.repository.MovieDiscussionRepository;
import org.viora.viorastreamingcore.configs.security.SecurityHelpers;
import org.viora.viorastreamingcore.content.repository.MovieRepository;

@Service
@RequiredArgsConstructor
public class MovieDiscussionService {

  private final ChatClient.Builder chatClientBuilder;
  private final MovieDiscussionRepository discussionRepository;
  private final DiscussionMessageRepository messageRepository;
  private final AccountRepository accountRepository;
  private final MovieRepository movieRepository;
  private final SecurityHelpers securityHelpers;

  @Transactional
  public DiscussionResponse getOrCreateDiscussion(CreateDiscussionRequest request) {
    Long accountId = securityHelpers.getCurrentlyAuthenticatedAccountId();
    return discussionRepository.findByAccountIdAndMovieId(accountId, request.movieId())
        .map(d -> new DiscussionResponse(d.getId(), d.getMovie().getId(), d.getCreatedAt()))
        .orElseGet(() -> {
          var account = accountRepository.getReferenceById(accountId);
          var movie = movieRepository.findById(request.movieId())
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
          var discussion = new MovieDiscussion(null, account, movie, Instant.now().toEpochMilli());
          discussionRepository.save(discussion);
          return new DiscussionResponse(discussion.getId(), movie.getId(), discussion.getCreatedAt());
        });
  }

  public List<MessageResponse> getMessages(Long discussionId) {
    verifyOwnership(discussionId);
    return messageRepository.findByDiscussionIdOrderByCreatedAtAsc(discussionId)
        .stream()
        .map(m -> new MessageResponse(m.getId(), m.getRole().name(), m.getContent(), m.getCreatedAt()))
        .toList();
  }

  @Transactional
  public MessageResponse sendMessage(Long discussionId, SendMessageRequest request) {
    MovieDiscussion discussion = verifyOwnership(discussionId);

    long now = Instant.now().toEpochMilli();
    DiscussionMessage userMsg = new DiscussionMessage(null, discussion, MessageRole.USER, request.content(), now);
    messageRepository.save(userMsg);

    List<DiscussionMessage> history = messageRepository.findByDiscussionIdOrderByCreatedAtAsc(discussionId);
    List<Message> aiMessages = new ArrayList<>();
    for (DiscussionMessage msg : history) {
      if (msg.getRole() == MessageRole.USER) {
        aiMessages.add(new UserMessage(msg.getContent()));
      } else {
        aiMessages.add(new AssistantMessage(msg.getContent()));
      }
    }

    String movieTitle = discussion.getMovie().getTitle();
    String moviePlot = discussion.getMovie().getPlot();

    String aiReply = chatClientBuilder.build().prompt()
        .system("You are a movie discussion assistant for the film \"" + movieTitle + "\". "
            + "Plot summary: " + moviePlot + ". "
            + "Engage with the user about this movie: answer questions, discuss themes, characters, and plot.")
        .messages(aiMessages)
        .call()
        .content();

    DiscussionMessage aiMsg = new DiscussionMessage(null, discussion, MessageRole.AI, aiReply, Instant.now().toEpochMilli());
    messageRepository.save(aiMsg);

    return new MessageResponse(aiMsg.getId(), aiMsg.getRole().name(), aiMsg.getContent(), aiMsg.getCreatedAt());
  }

  private MovieDiscussion verifyOwnership(Long discussionId) {
    Long accountId = securityHelpers.getCurrentlyAuthenticatedAccountId();
    MovieDiscussion discussion = discussionRepository.findById(discussionId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Discussion not found"));
    if (!discussion.getAccount().getId().equals(accountId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
    }
    return discussion;
  }
}
