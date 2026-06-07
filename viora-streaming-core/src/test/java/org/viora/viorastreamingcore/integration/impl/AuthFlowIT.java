package org.viora.viorastreamingcore.integration.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.viora.viorastreamingcore.configs.security.JwtTokenService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class AuthFlowIT {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserDetailsService userDetailsService;

  @MockitoBean
  private PasswordEncoder passwordEncoder;

  @MockitoBean
  private JwtTokenService jwtTokenService;

  @Test
  void shouldLoginSuccessfully() throws Exception {

    UserDetails user = User.withUsername("test@mail.com")
        .password("encoded")
        .disabled(false)
        .roles("USER")
        .build();

    when(userDetailsService.loadUserByUsername(any())).thenReturn(user);
    when(passwordEncoder.matches(any(), any())).thenReturn(true);
    when(jwtTokenService.generateToken(any())).thenReturn("jwt");

    mockMvc.perform(post("/api/v1/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                { "email": "test@mail.com", "password": "1234" }
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("jwt"));
  }
}