package org.viora.viorastreamingcore.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.viora.viorastreamingcore.configs.security.JwtAuthFilter;
import org.viora.viorastreamingcore.configs.security.SecurityConfigs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigsTest {

  @Mock
  private HttpSecurity httpSecurity;

  @Mock
  private JwtAuthFilter jwtAuthFilter;

  @InjectMocks
  private SecurityConfigs securityConfigs;

  @Test
  void whenSecurityFilterChain_thenConfigIsApplied() throws Exception {

    when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
    when(httpSecurity.formLogin(any())).thenReturn(httpSecurity);
    when(httpSecurity.httpBasic(any())).thenReturn(httpSecurity);
    when(httpSecurity.cors(any())).thenReturn(httpSecurity);
    when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
    when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);

    SecurityFilterChain result =
        securityConfigs.securityFilterChain(httpSecurity, jwtAuthFilter);

    assertThat(result).isNotNull();

    verify(httpSecurity).csrf(any());
    verify(httpSecurity).formLogin(any());
    verify(httpSecurity).httpBasic(any());
    verify(httpSecurity).cors(any());
    verify(httpSecurity).authorizeHttpRequests(any());
    verify(httpSecurity).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Test
  void whenPasswordEncoder_thenBCryptEncoderReturned() {
    PasswordEncoder encoder = securityConfigs.passwordEncoder();

    assertThat(encoder).isNotNull();
    assertThat(encoder.encode("test")).isNotBlank();
  }

  @Test
  void whenCorsConfiguration_thenReturnsValidSource() {
    // when
    CorsConfigurationSource source = securityConfigs.corsConfigurationSource();

    // then
    assertThat(source).isNotNull();

    // basic structural verification (no spring internals needed)
    assertThat(source.getClass().getName())
        .contains("UrlBasedCorsConfigurationSource");
  }
}