package org.viora.viorastreamingcore.integration.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.viora.viorastreamingcore.ai.dto.DiscussionResponse;
import org.viora.viorastreamingcore.ai.service.MovieDiscussionService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class AiFlowIT {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private MovieDiscussionService service;

  @Test
  void shouldCreateDiscussion() throws Exception {

    when(service.getOrCreateDiscussion(any()))
        .thenReturn(new DiscussionResponse(1L, 10L, 123L));

    mockMvc.perform(post("/api/v1/ai/discussions")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"movieId\": 10 }"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }
}