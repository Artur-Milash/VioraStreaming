package org.viora.viorastreamingcore.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.viora.viorastreamingcore.account.dto.Account;
import org.viora.viorastreamingcore.configs.security.SecurityHelpers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SecurityHelpersTest {

  private final SecurityHelpers securityHelpers = new SecurityHelpers();

  private MockedStatic<SecurityContextHolder> securityContextHolderMock;

  @AfterEach
  void tearDown() {
    if (securityContextHolderMock != null) {
      securityContextHolderMock.close();
    }
  }

  @Test
  void whenAuthenticated_thenReturnsAccountId() {

    // given
    Account account = mock(Account.class);
    when(account.getId()).thenReturn(42L);

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(account, null);

    SecurityContext securityContext = mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);

    securityContextHolderMock = mockStatic(SecurityContextHolder.class);
    securityContextHolderMock.when(SecurityContextHolder::getContext)
        .thenReturn(securityContext);

    // when
    Long result = securityHelpers.getCurrentlyAuthenticatedAccountId();

    // then
    assertThat(result).isEqualTo(42L);

    verify(account).getId();
    verify(securityContext).getAuthentication();
  }
}
