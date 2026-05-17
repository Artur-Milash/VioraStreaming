package org.viora.viorastreamingcore.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

  private final ChatClient chatClient;

  public TestController(ChatClient.Builder builder) {
    this.chatClient = builder.build();
  }

  @GetMapping
  public String test(@RequestParam(required = false) String question) {
    return chatClient.prompt()
        .system(
            "User has watched 'Iron Man 2008', so answer only questions related to that movie. Othrwise tell that you don't know.")
        .user(question)
        .call()
        .content();
  }

}
