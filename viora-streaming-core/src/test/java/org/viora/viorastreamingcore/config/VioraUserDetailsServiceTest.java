package org.viora.viorastreamingcore.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.viora.viorastreamingcore.account.dto.Account;
import org.viora.viorastreamingcore.account.service.GetUserAccountUseCase;
import org.viora.viorastreamingcore.configs.security.VioraUserDetailsService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VioraUserDetailsServiceTest {

  @Mock
  private GetUserAccountUseCase getUserAccountUseCase;

  @InjectMocks
  private VioraUserDetailsService userDetailsService;

  @Test
  void whenLoadUserByUsername_thenReturnsUserDetailsFromUseCase() {

    // given
    String username = "john";

    Account expectedAccount = mock(Account.class);

    when(getUserAccountUseCase.findAccountByLogin(username))
        .thenReturn(expectedAccount);

    UserDetails result = userDetailsService.loadUserByUsername(username);

    assertThat(result).isEqualTo(expectedAccount);

    verify(getUserAccountUseCase).findAccountByLogin(username);
  }

  @Test
  void whenLoadUserByUsername_thenPassesCorrectUsername() {

    // given
    String username = "alice";

    Account account = mock(Account.class);

    when(getUserAccountUseCase.findAccountByLogin(anyString()))
        .thenReturn(account);

    // when
    userDetailsService.loadUserByUsername(username);

    // then
    verify(getUserAccountUseCase).findAccountByLogin(username);
  }
}