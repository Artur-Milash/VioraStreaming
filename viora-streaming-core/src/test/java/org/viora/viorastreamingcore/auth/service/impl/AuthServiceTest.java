package org.viora.viorastreamingcore.auth.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.viora.viorastreamingcore.auth.dto.LoginUserRequest;
import org.viora.viorastreamingcore.auth.dto.LoginUserResponse;
import org.viora.viorastreamingcore.auth.exception.AccountDisabledException;
import org.viora.viorastreamingcore.auth.exception.InvalidCredentialsException;
import org.viora.viorastreamingcore.auth.service.AuthService;
import org.viora.viorastreamingcore.configs.security.JwtTokenService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private JwtTokenService jwtTokenService;

  @Mock
  private UserDetailsService userDetailsService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private AuthService authService;

  @Test
  void givenValidCredentialsAndEnabledAccount_whenLoginUser_thenReturnsJwtToken() {

    LoginUserRequest request =
        new LoginUserRequest("user@example.com", "password");

    UserDetails user = User.withUsername("user@example.com")
        .password("encoded-password")
        .authorities("USER")
        .disabled(false)
        .build();

    when(userDetailsService.loadUserByUsername("user@example.com"))
        .thenReturn(user);

    when(passwordEncoder.matches("password", "encoded-password"))
        .thenReturn(true);

    when(jwtTokenService.generateToken(user))
        .thenReturn("jwt-token");

    LoginUserResponse response =
        authService.loginUser(request);

    assertThat(response.token()).isEqualTo("jwt-token");
  }

  @Test
  void givenValidCredentials_whenLoginUser_thenLoadsUserByEmail() {

    LoginUserRequest request =
        new LoginUserRequest("user@example.com", "password");

    UserDetails user = User.withUsername("user@example.com")
        .password("encoded-password")
        .authorities("USER")
        .build();

    when(userDetailsService.loadUserByUsername("user@example.com"))
        .thenReturn(user);

    when(passwordEncoder.matches(anyString(), anyString()))
        .thenReturn(true);

    when(jwtTokenService.generateToken(any()))
        .thenReturn("token");

    authService.loginUser(request);

    verify(userDetailsService)
        .loadUserByUsername("user@example.com");
  }

  @Test
  void givenInvalidPassword_whenLoginUser_thenThrowsInvalidCredentialsException() {

    LoginUserRequest request =
        new LoginUserRequest("user@example.com", "wrong-password");

    UserDetails user = User.withUsername("user@example.com")
        .password("encoded-password")
        .authorities("USER")
        .build();

    when(userDetailsService.loadUserByUsername("user@example.com"))
        .thenReturn(user);

    when(passwordEncoder.matches("wrong-password", "encoded-password"))
        .thenReturn(false);

    InvalidCredentialsException exception =
        assertThrows(
            InvalidCredentialsException.class,
            () -> authService.loginUser(request));

    assertThat(exception.getMessage())
        .isEqualTo("Invalid credentials");

    verify(jwtTokenService, never())
        .generateToken(any());
  }

  @Test
  void givenDisabledAccount_whenLoginUser_thenThrowsAccountDisabledException() {

    LoginUserRequest request =
        new LoginUserRequest("user@example.com", "password");

    UserDetails disabledUser = User.withUsername("user@example.com")
        .password("encoded-password")
        .authorities("USER")
        .disabled(true)
        .build();

    when(userDetailsService.loadUserByUsername("user@example.com"))
        .thenReturn(disabledUser);

    when(passwordEncoder.matches("password", "encoded-password"))
        .thenReturn(true);

    AccountDisabledException exception =
        assertThrows(
            AccountDisabledException.class,
            () -> authService.loginUser(request));

    assertThat(exception.getMessage())
        .isEqualTo("Account is not verified");

    verify(jwtTokenService, never())
        .generateToken(any());
  }

  @Test
  void givenValidCredentials_whenLoginUser_thenPasswordIsVerified() {

    LoginUserRequest request =
        new LoginUserRequest("user@example.com", "password");

    UserDetails user = User.withUsername("user@example.com")
        .password("encoded-password")
        .authorities("USER")
        .build();

    when(userDetailsService.loadUserByUsername("user@example.com"))
        .thenReturn(user);

    when(passwordEncoder.matches("password", "encoded-password"))
        .thenReturn(true);

    when(jwtTokenService.generateToken(any()))
        .thenReturn("jwt-token");

    authService.loginUser(request);

    verify(passwordEncoder)
        .matches("password", "encoded-password");
  }

  @Test
  void givenValidCredentials_whenLoginUser_thenJwtGeneratedOnce() {

    LoginUserRequest request =
        new LoginUserRequest("user@example.com", "password");

    UserDetails user = User.withUsername("user@example.com")
        .password("encoded-password")
        .authorities("USER")
        .build();

    when(userDetailsService.loadUserByUsername("user@example.com"))
        .thenReturn(user);

    when(passwordEncoder.matches(anyString(), anyString()))
        .thenReturn(true);

    when(jwtTokenService.generateToken(user))
        .thenReturn("jwt-token");

    authService.loginUser(request);

    verify(jwtTokenService, times(1))
        .generateToken(user);
  }

  @Test
  void givenValidCredentials_whenLoginUser_thenResponseContainsGeneratedToken() {

    LoginUserRequest request =
        new LoginUserRequest("user@example.com", "password");

    UserDetails user = User.withUsername("user@example.com")
        .password("encoded-password")
        .authorities("USER")
        .build();

    when(userDetailsService.loadUserByUsername(anyString()))
        .thenReturn(user);

    when(passwordEncoder.matches(anyString(), anyString()))
        .thenReturn(true);

    when(jwtTokenService.generateToken(any()))
        .thenReturn("generated-jwt");

    LoginUserResponse response =
        authService.loginUser(request);

    assertThat(response).isNotNull();
    assertThat(response.token()).isEqualTo("generated-jwt");
  }
}
