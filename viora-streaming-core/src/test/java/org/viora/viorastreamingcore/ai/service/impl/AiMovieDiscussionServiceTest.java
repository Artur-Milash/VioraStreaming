package org.viora.viorastreamingcore.ai.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.viora.viorastreamingcore.account.model.AccountModel;
import org.viora.viorastreamingcore.account.repository.AccountRepository;
import org.viora.viorastreamingcore.ai.dto.CreateDiscussionRequest;
import org.viora.viorastreamingcore.ai.dto.DiscussionResponse;
import org.viora.viorastreamingcore.ai.dto.MessageResponse;
import org.viora.viorastreamingcore.ai.dto.SendMessageRequest;
import org.viora.viorastreamingcore.ai.model.DiscussionMessage;
import org.viora.viorastreamingcore.ai.model.MessageRole;
import org.viora.viorastreamingcore.ai.model.MovieDiscussion;
import org.viora.viorastreamingcore.ai.repository.DiscussionMessageRepository;
import org.viora.viorastreamingcore.ai.repository.MovieDiscussionRepository;
import org.viora.viorastreamingcore.ai.service.MovieDiscussionService;
import org.viora.viorastreamingcore.configs.security.SecurityHelpers;
import org.viora.viorastreamingcore.content.model.Movie;
import org.viora.viorastreamingcore.content.repository.MovieRepository;

@ExtendWith(MockitoExtension.class)
class MovieDiscussionServiceTest {

  @Mock
  private ChatClient.Builder chatClientBuilder;

  @Mock
  private ChatClient chatClient;

  @Mock
  private ChatClientRequestSpec requestSpec;

  @Mock
  private CallResponseSpec responseSpec;

  @Mock
  private MovieDiscussionRepository discussionRepository;

  @Mock
  private DiscussionMessageRepository messageRepository;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private MovieRepository movieRepository;

  @Mock
  private SecurityHelpers securityHelpers;

  @InjectMocks
  private MovieDiscussionService service;

  private AccountModel account;
  private Movie movie;
  private MovieDiscussion discussion;

  @BeforeEach
  void setup() {

    account = new AccountModel();
    account.setId(1L);

    movie = new Movie();
    movie.setId(100L);
    movie.setTitle("Interstellar");
    movie.setPlot("Space exploration");

    discussion = new MovieDiscussion(
        10L,
        account,
        movie,
        System.currentTimeMillis()
    );
  }

  @Test
  void givenExistingDiscussion_whenGetOrCreate_thenReturnsExistingDiscussion() {

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(1L);

    when(discussionRepository.findByAccountIdAndMovieId(1L, 100L))
        .thenReturn(Optional.of(discussion));

    DiscussionResponse result =
        service.getOrCreateDiscussion(new CreateDiscussionRequest(100L));

    assertThat(result.id()).isEqualTo(10L);
    assertThat(result.movieId()).isEqualTo(100L);

    verify(discussionRepository, never()).save(any());
  }

  @Test
  void givenNoDiscussion_whenGetOrCreate_thenCreatesDiscussion() {

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(1L);

    when(discussionRepository.findByAccountIdAndMovieId(1L, 100L))
        .thenReturn(Optional.empty());

    when(accountRepository.getReferenceById(1L))
        .thenReturn(account);

    when(movieRepository.findById(100L))
        .thenReturn(Optional.of(movie));

    when(discussionRepository.save(any(MovieDiscussion.class)))
        .thenAnswer(invocation -> {
          MovieDiscussion d = invocation.getArgument(0);
          d.setId(99L);
          return d;
        });

    DiscussionResponse result =
        service.getOrCreateDiscussion(new CreateDiscussionRequest(100L));

    assertThat(result.movieId()).isEqualTo(100L);

    verify(discussionRepository).save(any(MovieDiscussion.class));
  }

  @Test
  void givenMovieNotFound_whenGetOrCreate_thenThrows404() {

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(1L);

    when(discussionRepository.findByAccountIdAndMovieId(1L, 100L))
        .thenReturn(Optional.empty());

    when(accountRepository.getReferenceById(1L))
        .thenReturn(account);

    when(movieRepository.findById(100L))
        .thenReturn(Optional.empty());

    ResponseStatusException ex = assertThrows(
        ResponseStatusException.class,
        () -> service.getOrCreateDiscussion(
            new CreateDiscussionRequest(100L)));

    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }


  @Test
  void givenOwnedDiscussion_whenGetMessages_thenReturnsMessages() {

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(1L);

    when(discussionRepository.findById(10L))
        .thenReturn(Optional.of(discussion));

    DiscussionMessage userMessage =
        new DiscussionMessage(
            1L,
            discussion,
            MessageRole.USER,
            "Hello",
            111L);

    when(messageRepository.findByDiscussionIdOrderByCreatedAtAsc(10L))
        .thenReturn(List.of(userMessage));

    List<MessageResponse> result =
        service.getMessages(10L);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).content()).isEqualTo("Hello");
    assertThat(result.get(0).role()).isEqualTo("USER");
  }

  @Test
  void givenDifferentOwner_whenGetMessages_thenThrows403() {

    AccountModel other = new AccountModel();
    other.setId(999L);

    discussion.setAccount(other);

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(1L);

    when(discussionRepository.findById(10L))
        .thenReturn(Optional.of(discussion));

    ResponseStatusException ex = assertThrows(
        ResponseStatusException.class,
        () -> service.getMessages(10L));

    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
  }

  @Test
  void givenValidDiscussion_whenSendMessage_thenStoresUserAndAiMessages() {

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(1L);

    when(discussionRepository.findById(10L))
        .thenReturn(Optional.of(discussion));

    DiscussionMessage existing =
        new DiscussionMessage(
            1L,
            discussion,
            MessageRole.USER,
            "What is the ending?",
            100L);

    when(messageRepository.findByDiscussionIdOrderByCreatedAtAsc(10L))
        .thenReturn(List.of(existing));

    when(chatClientBuilder.build()).thenReturn(chatClient);

    when(chatClient.prompt()).thenReturn(requestSpec);

    when(requestSpec.system(anyString()))
        .thenReturn(requestSpec);

    when(requestSpec.messages(anyList()))
        .thenReturn(requestSpec);

    when(requestSpec.call())
        .thenReturn(responseSpec);

    when(responseSpec.content())
        .thenReturn("The ending is intentionally ambiguous.");

    when(messageRepository.save(any(DiscussionMessage.class)))
        .thenAnswer(invocation -> {
          DiscussionMessage msg = invocation.getArgument(0);

          if (msg.getRole() == MessageRole.AI) {
            msg.setId(999L);
          }

          return msg;
        });

    MessageResponse result =
        service.sendMessage(
            10L,
            new SendMessageRequest("Explain ending"));

    assertThat(result.role()).isEqualTo("AI");
    assertThat(result.content())
        .isEqualTo("The ending is intentionally ambiguous.");

    verify(messageRepository, times(2))
        .save(any(DiscussionMessage.class));
  }

  @Test
  void givenValidDiscussion_whenSendMessage_thenPromptContainsMovieInfo() {

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(1L);

    when(discussionRepository.findById(10L))
        .thenReturn(Optional.of(discussion));

    when(messageRepository.findByDiscussionIdOrderByCreatedAtAsc(10L))
        .thenReturn(List.of());

    when(chatClientBuilder.build()).thenReturn(chatClient);

    when(chatClient.prompt()).thenReturn(requestSpec);

    when(requestSpec.system(anyString()))
        .thenReturn(requestSpec);

    when(requestSpec.messages(anyList()))
        .thenReturn(requestSpec);

    when(requestSpec.call())
        .thenReturn(responseSpec);

    when(responseSpec.content())
        .thenReturn("AI reply");

    service.sendMessage(
        10L,
        new SendMessageRequest("Question"));

    ArgumentCaptor<String> captor =
        ArgumentCaptor.forClass(String.class);

    verify(requestSpec).system(captor.capture());

    assertThat(captor.getValue())
        .contains("Interstellar")
        .contains("Space exploration");
  }

  @Test
  void givenDiscussionNotFound_whenSendMessage_thenThrows404() {

    when(securityHelpers.getCurrentlyAuthenticatedAccountId())
        .thenReturn(1L);

    when(discussionRepository.findById(10L))
        .thenReturn(Optional.empty());

    ResponseStatusException ex = assertThrows(
        ResponseStatusException.class,
        () -> service.sendMessage(
            10L,
            new SendMessageRequest("Hello")));

    assertThat(ex.getStatusCode())
        .isEqualTo(HttpStatus.NOT_FOUND);
  }
}