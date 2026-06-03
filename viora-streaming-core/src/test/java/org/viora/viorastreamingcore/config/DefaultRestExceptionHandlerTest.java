package org.viora.viorastreamingcore.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.viora.viorastreamingcore.configs.handlers.ApiError;
import org.viora.viorastreamingcore.configs.handlers.DefaultRestExceptionHandler;
import org.viora.viorastreamingcore.exceptions.EntityConflictException;
import org.viora.viorastreamingcore.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DefaultRestExceptionHandlerTest {

  private DefaultRestExceptionHandler exceptionHandler;

  @BeforeEach
  void setUp() {
    exceptionHandler = new DefaultRestExceptionHandler();
  }

  @Test
  void whenHandleEntityNotFoundException_thenReturnsNotFoundResponse() {
    // given
    String message = "Movie not found";
    EntityNotFoundException exception = new EntityNotFoundException(message);

    // when
    ResponseEntity<ApiError> response =
        exceptionHandler.handleEntityNotFoundException(exception);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().errorCode()).isEqualTo(404);
    assertThat(response.getBody().message()).isEqualTo(message);
    assertThat(response.getBody().timeStamp()).isNotNull();
  }

  @Test
  void whenHandleEntityConflictException_thenReturnsConflictResponse() {
    // given
    String message = "Entity already exists";
    EntityConflictException exception = new EntityConflictException(message);

    // when
    ResponseEntity<ApiError> response =
        exceptionHandler.handleEntityConflictException(exception);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().errorCode()).isEqualTo(409);
    assertThat(response.getBody().message()).isEqualTo(message);
    assertThat(response.getBody().timeStamp()).isNotNull();
  }

  @Test
  void whenHandleMethodArgumentNotValidException_thenReturnsBadRequestWithFieldErrors() {
    // given
    BindingResult bindingResult = mock(BindingResult.class);

    FieldError emailError =
        new FieldError("user", "email", "Email is invalid");

    FieldError passwordError =
        new FieldError("user", "password", "Password is required");

    when(bindingResult.getFieldErrors())
        .thenReturn(List.of(emailError, passwordError));

    MethodArgumentNotValidException exception =
        mock(MethodArgumentNotValidException.class);

    when(exception.getBindingResult()).thenReturn(bindingResult);

    // when
    ResponseEntity<Map<String, String>> response =
        exceptionHandler.handleMethodArgumentNotValidException(exception);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).hasSize(2);
    assertThat(response.getBody())
        .containsEntry("email", "Email is invalid")
        .containsEntry("password", "Password is required");

    verify(exception).getBindingResult();
    verify(bindingResult).getFieldErrors();
  }

  @Test
  void whenDuplicateFieldErrorsExist_thenKeepsFirstValue() {
    // given
    BindingResult bindingResult = mock(BindingResult.class);

    FieldError firstError =
        new FieldError("user", "email", "First error");

    FieldError secondError =
        new FieldError("user", "email", "Second error");

    when(bindingResult.getFieldErrors())
        .thenReturn(List.of(firstError, secondError));

    MethodArgumentNotValidException exception =
        mock(MethodArgumentNotValidException.class);

    when(exception.getBindingResult()).thenReturn(bindingResult);

    // when
    ResponseEntity<Map<String, String>> response =
        exceptionHandler.handleMethodArgumentNotValidException(exception);

    // then
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).hasSize(1);
    assertThat(response.getBody().get("email"))
        .isEqualTo("First error");
  }

}