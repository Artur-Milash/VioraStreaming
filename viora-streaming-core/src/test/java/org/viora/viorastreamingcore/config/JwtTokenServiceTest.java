package org.viora.viorastreamingcore.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.viora.viorastreamingcore.configs.security.JwtTokenService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

  @Mock
  private JwtEncoder encoder;

  @Mock
  private JwtDecoder decoder;

  @Mock
  private JwtEncoderParameters jwtEncoderParameters;

  @InjectMocks
  private JwtTokenService jwtTokenService;

  private UserDetails userDetails;

  @BeforeEach
  void setUp() {
    userDetails = new User(
        "john",
        "password",
        List.of()
    );
  }

  @Test
  void whenGenerateToken_thenReturnsEncodedToken() {
    // given
    Jwt encodedJwt = mock(Jwt.class);

    when(encodedJwt.getTokenValue()).thenReturn("generated-token");
    when(encoder.encode(any(JwtEncoderParameters.class)))
        .thenReturn(encodedJwt);

    // when
    String result = jwtTokenService.generateToken(userDetails);

    // then
    assertThat(result).isEqualTo("generated-token");

    ArgumentCaptor<JwtEncoderParameters> captor =
        ArgumentCaptor.forClass(JwtEncoderParameters.class);

    verify(encoder).encode(captor.capture());

    assertThat(captor.getValue()).isNotNull();
  }

  @Test
  void whenTokenIsValidAndNotExpired_thenReturnsTrue() {
    // given
    Jwt jwt = mock(Jwt.class);

    when(jwt.getExpiresAt())
        .thenReturn(Instant.now().plus(1, ChronoUnit.HOURS));

    when(decoder.decode("valid-token")).thenReturn(jwt);

    // when
    boolean result = jwtTokenService.isValidToken("valid-token");

    // then
    assertThat(result).isTrue();

    verify(decoder).decode("valid-token");
  }

  @Test
  void whenTokenIsExpired_thenReturnsFalse() {
    // given
    Jwt jwt = mock(Jwt.class);

    when(jwt.getExpiresAt())
        .thenReturn(Instant.now().minus(1, ChronoUnit.MINUTES));

    when(decoder.decode("expired-token")).thenReturn(jwt);

    // when
    boolean result = jwtTokenService.isValidToken("expired-token");

    // then
    assertThat(result).isFalse();

    verify(decoder).decode("expired-token");
  }

  @Test
  void whenDecoderThrowsJwtValidationException_thenReturnsFalse() {
    // given
    when(decoder.decode("invalid-token"))
        .thenThrow(new JwtValidationException(
            "Invalid token",
            List.of()
        ));

    // when
    boolean result = jwtTokenService.isValidToken("invalid-token");

    // then
    assertThat(result).isFalse();

    verify(decoder).decode("invalid-token");
  }

  @Test
  void whenGenerateDropPasswordToken_thenReturnsEncodedToken() {
    // given
    Jwt encodedJwt = mock(Jwt.class);

    when(encodedJwt.getTokenValue()).thenReturn("drop-password-token");
    when(encoder.encode(any(JwtEncoderParameters.class)))
        .thenReturn(encodedJwt);

    // when
    String result =
        jwtTokenService.generateDropPasswordToken(userDetails);

    // then
    assertThat(result).isEqualTo("drop-password-token");

    verify(encoder).encode(any(JwtEncoderParameters.class));
  }

  @Test
  void whenGetUsernameFromDropPasswordTokenWithValidPurpose_thenReturnsUsername() {
    // given
    Jwt jwt = mock(Jwt.class);

    when(jwt.getClaimAsString("purpose"))
        .thenReturn("drop-password");

    when(jwt.getSubject()).thenReturn("john");

    when(decoder.decode("drop-token")).thenReturn(jwt);

    // when
    String result =
        jwtTokenService.getUsernameFromDropPasswordToken("drop-token");

    // then
    assertThat(result).isEqualTo("john");

    verify(decoder).decode("drop-token");
  }

  @Test
  void whenGetUsernameFromDropPasswordTokenWithInvalidPurpose_thenThrowsException() {
    // given
    Jwt jwt = mock(Jwt.class);

    when(jwt.getClaimAsString("purpose"))
        .thenReturn("access-token");

    when(decoder.decode("invalid-purpose-token"))
        .thenReturn(jwt);

    // when / then
    assertThatThrownBy(() ->
        jwtTokenService.getUsernameFromDropPasswordToken(
            "invalid-purpose-token"
        )
    )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid token purpose");

    verify(decoder).decode("invalid-purpose-token");
  }

  @Test
  void whenGetUsernameFromToken_thenReturnsSubject() {
    // given
    Jwt jwt = mock(Jwt.class);

    when(jwt.getSubject()).thenReturn("john");

    when(decoder.decode("token")).thenReturn(jwt);

    // when
    String result = jwtTokenService.getUsernameFromToken("token");

    // then
    assertThat(result).isEqualTo("john");

    verify(decoder).decode("token");
  }

}