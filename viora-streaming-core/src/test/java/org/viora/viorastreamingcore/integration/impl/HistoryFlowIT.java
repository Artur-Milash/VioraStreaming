package org.viora.viorastreamingcore.integration.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.viora.viorastreamingcore.history.service.GetHistoryUseCase;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class HistoryFlowIT {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private GetHistoryUseCase historyUseCase;

  @Test
  void shouldReturnHistory() throws Exception {

    when(historyUseCase.getHistory()).thenReturn(List.of());

    mockMvc.perform(get("/api/v1/history"))
        .andExpect(status().isOk());
  }
}