package org.viora.viorastreamingcore.auth.controller.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.viora.viorastreamingcore.auth.controller.AuthController;
import org.viora.viorastreamingcore.auth.dto.LoginUserRequest;
import org.viora.viorastreamingcore.auth.dto.LoginUserResponse;
import org.viora.viorastreamingcore.auth.service.LoginUserUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock
  private LoginUserUseCase loginUserUseCase;

  @InjectMocks
  private AuthController authController;

  @Test
  void givenValidRequest_whenAuthenticate_thenReturnsToken() {
    // given
    LoginUserRequest request = new LoginUserRequest(
        "user@example.com",
        "password123"
    );

    LoginUserResponse response = new LoginUserResponse("jwt-token-123");

    when(loginUserUseCase.loginUser(request)).thenReturn(response);

    // when
    var result = authController.authenticate(request);

    // then
    assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(result.getBody()).isNotNull();
    assertThat(result.getBody().token()).isEqualTo("jwt-token-123");

    verify(loginUserUseCase).loginUser(request);
  }

  @Test
  void givenValidRequest_whenAuthenticate_thenDelegatesToUseCase() {
    // given
    LoginUserRequest request = new LoginUserRequest(
        "test@example.com",
        "secret"
    );

    when(loginUserUseCase.loginUser(any())).thenReturn(
        new LoginUserResponse("token")
    );

    // when
    authController.authenticate(request);

    // then
    ArgumentCaptor<LoginUserRequest> captor =
        ArgumentCaptor.forClass(LoginUserRequest.class);

    verify(loginUserUseCase, times(1)).loginUser(captor.capture());

    assertThat(captor.getValue().email()).isEqualTo("test@example.com");
    assertThat(captor.getValue().password()).isEqualTo("secret");
  }

  @Test
  void givenDifferentResponseFromUseCase_whenAuthenticate_thenControllerReturnsSameResponse() {
    // given
    LoginUserRequest request = new LoginUserRequest(
        "user@example.com",
        "password123"
    );

    LoginUserResponse response = new LoginUserResponse("different-token");

    when(loginUserUseCase.loginUser(request)).thenReturn(response);

    // when
    var result = authController.authenticate(request);

    // then
    assertThat(result.getBody().token()).isEqualTo("different-token");
  }
}
