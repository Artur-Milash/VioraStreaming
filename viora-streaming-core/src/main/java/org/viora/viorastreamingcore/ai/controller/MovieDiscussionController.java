package org.viora.viorastreamingcore.ai.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.viora.viorastreamingcore.ai.dto.CreateDiscussionRequest;
import org.viora.viorastreamingcore.ai.dto.DiscussionResponse;
import org.viora.viorastreamingcore.ai.dto.MessageResponse;
import org.viora.viorastreamingcore.ai.dto.SendMessageRequest;
import org.viora.viorastreamingcore.ai.service.MovieDiscussionService;

@RestController
@RequestMapping("/api/v1/ai/discussions")
@RequiredArgsConstructor
public class MovieDiscussionController {

  private final MovieDiscussionService discussionService;

  @PostMapping
  public DiscussionResponse getOrCreateDiscussion(@Valid @RequestBody CreateDiscussionRequest request) {
    return discussionService.getOrCreateDiscussion(request);
  }

  @GetMapping("/{discussionId}/messages")
  public List<MessageResponse> getMessages(@PathVariable Long discussionId) {
    return discussionService.getMessages(discussionId);
  }

  @PostMapping("/{discussionId}/messages")
  public MessageResponse sendMessage(
      @PathVariable Long discussionId,
      @Valid @RequestBody SendMessageRequest request) {
    return discussionService.sendMessage(discussionId, request);
  }
}
