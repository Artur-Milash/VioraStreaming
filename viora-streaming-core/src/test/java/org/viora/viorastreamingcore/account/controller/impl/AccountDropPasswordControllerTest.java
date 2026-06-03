package org.viora.viorastreamingcore.account.controller.impl;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.viora.viorastreamingcore.account.controller.DropPasswordController;
import org.viora.viorastreamingcore.account.dto.DropPasswordRequest;
import org.viora.viorastreamingcore.account.service.DropPasswordUseCase;
import org.viora.viorastreamingcore.verification.dto.VerificationType;
import org.viora.viorastreamingcore.verification.service.VerificationService;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountDropPasswordControllerTest {

  private final DropPasswordUseCase dropPasswordUseCase = mock(DropPasswordUseCase.class);

  private final VerificationService verificationService = mock(VerificationService.class);

  private final MockMvc mockMvc = MockMvcBuilders
      .standaloneSetup(new DropPasswordController(
          dropPasswordUseCase,
          verificationService
      ))
      .build();

  @Test
  void givenValidEmail_whenSendDropPasswordRequest_thenNoContentReturned() throws Exception {
    // given
    String email = "user@example.com";

    // when & then
    mockMvc.perform(get("/api/v1/accounts/drop-password")
            .param("email", email))
        .andExpect(status().isNoContent());

    verify(dropPasswordUseCase).dropPassword(email);
    verifyNoMoreInteractions(dropPasswordUseCase);
  }

  @Test
  void givenValidRequestAndToken_whenUpdateUserPassword_thenVerificationCalled()
      throws Exception {

    // given
    String token = "token-123";
    String password = "newPassword";

    String requestBody = """
                {
                  "password": "newPassword"
                }
                """;

    // when & then
    mockMvc.perform(post("/api/v1/accounts/drop-password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
            .cookie(new jakarta.servlet.http.Cookie(
                DropPasswordController.DROP_PASSWORD_TOKEN,
                token
            )))
        .andExpect(status().isNoContent());

    verify(verificationService).verify(
        eq(VerificationType.VERIFY_DROP_PASSWORD),
        eq(token),
        any()
    );
  }

  @Test
  void givenValidRequest_whenUpdateUserPassword_thenPasswordUpdatedUsingVerifiedEmail()
      throws Exception {

    // given
    String token = "token-123";

    String requestBody = """
                {
                  "password": "strongPassword"
                }
                """;

    doAnswer(invocation -> {
      Consumer<Object> callback = invocation.getArgument(2);
      callback.accept("verified@example.com");
      return null;
    }).when(verificationService).verify(
        eq(VerificationType.VERIFY_DROP_PASSWORD),
        eq(token),
        any()
    );

    // when
    mockMvc.perform(post("/api/v1/accounts/drop-password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
            .cookie(new Cookie(
                DropPasswordController.DROP_PASSWORD_TOKEN,
                token
            )))
        .andExpect(status().isNoContent());

    // then
    verify(dropPasswordUseCase)
        .updatePassword("verified@example.com", "strongPassword");
  }

  @Test
  void givenMissingToken_whenUpdateUserPassword_thenVerificationCalledWithNullToken()
      throws Exception {

    // given
    String requestBody = """
                {
                  "password": "password123"
                }
                """;

    // when
    mockMvc.perform(post("/api/v1/accounts/drop-password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isNoContent());

    // then
    verify(verificationService).verify(
        eq(VerificationType.VERIFY_DROP_PASSWORD),
        isNull(),
        any()
    );
  }

  @Test
  void givenEmptyPassword_whenUpdateUserPassword_thenBadRequestReturned()
      throws Exception {

    // given
    String requestBody = """
                {
                  "password": ""
                }
                """;

    // when & then
    mockMvc.perform(post("/api/v1/accounts/drop-password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isBadRequest());

    verifyNoInteractions(verificationService);
    verifyNoInteractions(dropPasswordUseCase);
  }
}