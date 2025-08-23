package com.github.hsuliz.aiagentsample.domain;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SpellChecker {

  private final ChatClient chatClient;

  public SpellChecker(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  private static final String PROMPT = "Spellcheck given text. Return only text.";

  public List<String> check(List<String> texts) {
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      List<CompletableFuture<String>> futures =
          texts.stream()
              .map(
                  text ->
                      CompletableFuture.supplyAsync(
                          () -> {
                            try {
                              System.out.println(Thread.currentThread());
                              return chatClient.prompt(PROMPT + " Text: " + text).call().content();
                            } catch (Exception e) {
                              throw new RuntimeException("Failed to process:", e);
                            }
                          },
                          executor))
              .toList();

      // Wait for all tasks to complete
      CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();

      return futures.stream().map(CompletableFuture::join).toList();
    }
  }
}
