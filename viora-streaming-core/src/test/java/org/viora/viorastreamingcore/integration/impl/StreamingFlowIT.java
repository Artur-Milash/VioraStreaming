package org.viora.viorastreamingcore.integration.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.viora.viorastreamingcore.configs.security.SecurityHelpers;
import org.viora.viorastreamingcore.content.dto.MovieSummary;
import org.viora.viorastreamingcore.content.service.MovieService;

import org.viora.viorastreamingcore.history.service.command.SaveHistoryCommand;
import org.viora.viorastreamingcore.streaming.repository.StreamingRepository;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class StreamingFlowIT {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StreamingRepository streamingRepository;

  @MockitoBean
  private MovieService movieService;

  @MockitoBean
  private SecurityHelpers securityHelpers;

  @MockitoBean
  private ApplicationEventPublisher publisher;

  @Test
  void shouldStreamAndPublishEvent() throws Exception {

    when(streamingRepository.getMovieSegment(any(), any()))
        .thenReturn(new ByteArrayResource("data".getBytes()));

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(1L);

    MovieSummary summary = mock(MovieSummary.class);
    when(summary.id()).thenReturn(10L);
    when(movieService.getMovieByImdbId(any())).thenReturn(summary);

    mockMvc.perform(get("/api/v1/streaming/movies/tt123/segment_1.ts"))
        .andExpect(status().isOk());

    verify(publisher).publishEvent(any(SaveHistoryCommand.class));
  }
}
