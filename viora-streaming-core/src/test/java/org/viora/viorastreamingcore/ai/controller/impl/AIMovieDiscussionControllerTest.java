package org.viora.viorastreamingcore.ai.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.viora.viorastreamingcore.ai.controller.MovieDiscussionController;
import org.viora.viorastreamingcore.ai.dto.CreateDiscussionRequest;
import org.viora.viorastreamingcore.ai.dto.DiscussionResponse;
import org.viora.viorastreamingcore.ai.dto.MessageResponse;
import org.viora.viorastreamingcore.ai.dto.SendMessageRequest;
import org.viora.viorastreamingcore.ai.service.MovieDiscussionService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AIMovieDiscussionControllerTest {

  private final MovieDiscussionService discussionService = mock(MovieDiscussionService.class);

  private final ObjectMapper objectMapper = new ObjectMapper();

  private final MockMvc mockMvc = MockMvcBuilders
      .standaloneSetup(new MovieDiscussionController(discussionService))
      .build();

  @Test
  void givenValidRequest_whenGetOrCreateDiscussion_thenDiscussionReturned() throws Exception {
    // given
    CreateDiscussionRequest request = new CreateDiscussionRequest(10L);

    DiscussionResponse response =
        new DiscussionResponse(1L, 10L, 123456789L);

    when(discussionService.getOrCreateDiscussion(any(CreateDiscussionRequest.class)))
        .thenReturn(response);

    // when & then
    mockMvc.perform(post("/api/v1/ai/discussions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.movieId").value(10))
        .andExpect(jsonPath("$.createdAt").value(123456789));

    verify(discussionService).getOrCreateDiscussion(any(CreateDiscussionRequest.class));
    verifyNoMoreInteractions(discussionService);
  }

  @Test
  void givenValidDiscussionId_whenGetMessages_thenMessagesReturned() throws Exception {
    // given
    Long discussionId = 5L;

    MessageResponse message =
        new MessageResponse(1L, "USER", "Hello", 111111L);

    when(discussionService.getMessages(discussionId))
        .thenReturn(List.of(message));

    // when & then
    mockMvc.perform(get("/api/v1/ai/discussions/{discussionId}/messages", discussionId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].role").value("USER"))
        .andExpect(jsonPath("$[0].content").value("Hello"))
        .andExpect(jsonPath("$[0].createdAt").value(111111));

    verify(discussionService).getMessages(discussionId);
    verifyNoMoreInteractions(discussionService);
  }

  @Test
  void givenValidRequest_whenSendMessage_thenMessageReturned() throws Exception {
    // given
    Long discussionId = 7L;

    SendMessageRequest request = new SendMessageRequest("Hi AI");

    MessageResponse response =
        new MessageResponse(2L, "AI", "Hello human", 222222L);

    when(discussionService.sendMessage(eq(discussionId), any(SendMessageRequest.class)))
        .thenReturn(response);

    // when & then
    mockMvc.perform(post("/api/v1/ai/discussions/{discussionId}/messages", discussionId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.role").value("AI"))
        .andExpect(jsonPath("$.content").value("Hello human"))
        .andExpect(jsonPath("$.createdAt").value(222222));

    verify(discussionService).sendMessage(eq(discussionId), any(SendMessageRequest.class));
    verifyNoMoreInteractions(discussionService);
  }

  @Test
  void givenBlankMessage_whenSendMessage_thenBadRequestReturned() throws Exception {
    // given
    Long discussionId = 7L;

    String requestBody = """
                {
                  "content": ""
                }
                """;

    // when & then
    mockMvc.perform(post("/api/v1/ai/discussions/{discussionId}/messages", discussionId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isBadRequest());

    verifyNoInteractions(discussionService);
  }

  @Test
  void givenMissingDiscussionId_whenGetMessages_thenBadRequestOrNotFoundBehavior() throws Exception {
    // This depends on Spring mapping; usually 404 or 400, but we assert service not called

    mockMvc.perform(get("/api/v1/ai/discussions//messages"))
        .andExpect(status().is4xxClientError());

    verifyNoInteractions(discussionService);
  }
}