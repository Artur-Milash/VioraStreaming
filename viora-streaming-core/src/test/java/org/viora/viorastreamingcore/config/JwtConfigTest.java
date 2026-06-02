package org.viora.viorastreamingcore.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.viora.viorastreamingcore.configs.security.JwtConfig;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class JwtConfigTest {

  private JwtConfig jwtConfig;

  @BeforeEach
  void setUp() throws Exception {
    jwtConfig = new JwtConfig();

    Field jwtKeyField = JwtConfig.class.getDeclaredField("jwtKey");
    jwtKeyField.setAccessible(true);
    jwtKeyField.set(jwtConfig, "my-secret-key-my-secret-key");
  }

  @Test
  void whenJwtEncoderCreated_thenReturnsNimbusJwtEncoder() {
    // when
    JwtEncoder jwtEncoder = jwtConfig.jwtEncoder();

    // then
    assertThat(jwtEncoder).isNotNull();
    assertThat(jwtEncoder.getClass().getSimpleName())
        .isEqualTo("NimbusJwtEncoder");
  }

  @Test
  void whenJwtDecoderCreated_thenReturnsNimbusJwtDecoder() {
    // when
    JwtDecoder jwtDecoder = jwtConfig.jwtDecoder();

    // then
    assertThat(jwtDecoder).isNotNull();
    assertThat(jwtDecoder.getClass().getSimpleName())
        .isEqualTo("NimbusJwtDecoder");
  }

}