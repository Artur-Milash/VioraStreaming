package org.viora.viorastreamingcore.account.controller.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.viora.viorastreamingcore.account.controller.AccountsController;
import org.viora.viorastreamingcore.account.dto.AccountDto;
import org.viora.viorastreamingcore.account.dto.RegisterUserRequest;
import org.viora.viorastreamingcore.account.dto.UpdateAccountRequest;
import org.viora.viorastreamingcore.account.service.GetUserAccountUseCase;
import org.viora.viorastreamingcore.account.service.RegisterUserUseCase;
import org.viora.viorastreamingcore.account.service.UpdateAccountUseCase;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

  @Mock
  private RegisterUserUseCase registerUserUseCase;

  @Mock
  private UpdateAccountUseCase updateAccountUseCase;

  @Mock
  private GetUserAccountUseCase getUserAccountUseCase;

  @InjectMocks
  private AccountsController accountsController;

  @Test
  void givenValidRequest_whenRegisterUser_thenReturnsCreatedStatus() {
    // given
    RegisterUserRequest request = new RegisterUserRequest(
        "user@example.com",
        "password123"
    );

    // when
    ResponseEntity<Void> response = accountsController.registerUser(request);

    // then
    ArgumentCaptor<RegisterUserRequest> captor =
        ArgumentCaptor.forClass(RegisterUserRequest.class);

    verify(registerUserUseCase).registerUser(captor.capture());

    assertThat(captor.getValue().email()).isEqualTo("user@example.com");
    assertThat(captor.getValue().password()).isEqualTo("password123");

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNull();
  }

  @Test
  void givenExistingAccount_whenGetUserAccount_thenReturnsAccountDto() {
    // given
    AccountDto accountDto = AccountDto.builder()
        .email("user@example.com")
        .fullName("John Doe")
        .bio("Bio")
        .build();

    when(getUserAccountUseCase.getUserAccount()).thenReturn(accountDto);

    // when
    ResponseEntity<AccountDto> response = accountsController.getUserAccount();

    // then
    verify(getUserAccountUseCase).getUserAccount();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(accountDto);
  }

  @Test
  void givenUpdateRequest_whenUpdateUserAccount_thenReturnsUpdatedAccount() {
    // given
    UpdateAccountRequest request = new UpdateAccountRequest(
        "Updated User",
        "Updated bio"
    );

    AccountDto updatedAccount = AccountDto.builder()
        .email("user@example.com")
        .fullName("Updated User")
        .bio("Updated bio")
        .build();

    when(updateAccountUseCase.updateUser(request)).thenReturn(updatedAccount);

    // when
    ResponseEntity<AccountDto> response =
        accountsController.updateUserAccount(request);

    // then
    ArgumentCaptor<UpdateAccountRequest> captor =
        ArgumentCaptor.forClass(UpdateAccountRequest.class);

    verify(updateAccountUseCase).updateUser(captor.capture());

    assertThat(captor.getValue().fullName()).isEqualTo("Updated User");
    assertThat(captor.getValue().bio()).isEqualTo("Updated bio");

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(updatedAccount);
  }

  @Test
  void givenAuthenticatedUser_whenDeleteUserAccount_thenReturnsNoContent() {
    // when
    ResponseEntity<Void> response = accountsController.deleteUserAccount();

    // then
    verify(updateAccountUseCase).deleteAccount();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(response.getBody()).isNull();
  }

}