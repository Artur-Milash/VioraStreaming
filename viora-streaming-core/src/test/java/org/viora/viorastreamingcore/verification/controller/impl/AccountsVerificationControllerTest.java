package org.viora.viorastreamingcore.verification.controller.impl;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.viora.viorastreamingcore.verification.controller.AccountsVerificationController;
import org.viora.viorastreamingcore.verification.dto.VerificationType;
import org.viora.viorastreamingcore.verification.service.VerificationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountsVerificationControllerTest {

  @Mock
  private VerificationService verificationService;

  @Mock
  private HttpServletResponse response;

  @InjectMocks
  private AccountsVerificationController controller;

  @Test
  void givenToken_whenVerifyUserAccount_thenRedirectsAndVerifiesEmail() {

    // given
    String token = "email-token";
    String callback = "http://client/register-success";

    // when
    String result = controller.verifyUserAccount(token, callback);

    // then
    assertThat(result).isEqualTo("redirect:" + callback);

    verify(verificationService)
        .verify(VerificationType.VERIFY_EMAIL, token);
  }

  @Test
  void givenToken_whenVerifyDropPassword_thenSetsCookieAndRedirects() {
    // given
    String token = "drop-token";
    String callback = "http://client/drop-success";

    // when
    String result = controller.verifyDropPassword(token, callback, response);

    // then
    assertThat(result).isEqualTo("redirect:" + callback);

    verify(verificationService)
        .verify(VerificationType.VERIFY_DROP_PASSWORD, token);

    ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
    verify(response).addCookie(cookieCaptor.capture());

    Cookie cookie = cookieCaptor.getValue();

    assertThat(cookie.getName()).isEqualTo("DROP_PASSWORD_TOKEN");
    assertThat(cookie.getValue()).isEqualTo(token);
    assertThat(cookie.getMaxAge()).isEqualTo(15 * 60);
    assertThat(cookie.isHttpOnly()).isTrue();
    assertThat(cookie.getPath()).isEqualTo("/");
  }

  @Test
  void givenDropPasswordToken_whenVerify_thenCallsServiceWithCorrectType() {
    // given
    String token = "abc-token";

    // when
    controller.verifyDropPassword(token, "cb", response);

    // then
    ArgumentCaptor<VerificationType> typeCaptor =
        ArgumentCaptor.forClass(VerificationType.class);

    ArgumentCaptor<String> tokenCaptor =
        ArgumentCaptor.forClass(String.class);

    verify(verificationService).verify(typeCaptor.capture(), tokenCaptor.capture());

    assertThat(typeCaptor.getValue())
        .isEqualTo(VerificationType.VERIFY_DROP_PASSWORD);

    assertThat(tokenCaptor.getValue()).isEqualTo(token);
  }
}
