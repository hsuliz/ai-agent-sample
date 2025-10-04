package com.github.hsuliz.aiagentsample.domain.orchestrator;

import com.github.hsuliz.aiagentsample.domain.AIAgent;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;

@Component
public class Orchestrator implements AIAgent<List<String>> {

  private final Logger logger = LoggerFactory.getLogger(Orchestrator.class);

  public static final String SYSTEM_PROMPT =
      """
                  Analyze given text and break it down into 3 distinct approaches.
                  Return your response in this JSON format. Values of it is example.
                  \\{
                  "analysis": "Explain your understanding of the task and which variations would be valuable.
                               Focus on how each approach serves different aspects of the task.",
                  "tasks": [
                      \\{
                      "type": "...", //Type can be only 1 word
                      "description": "..."
                      \\}
                  ]
                  \\}
                  """;
  public static final String WORKER_PROMPT =
      """
                  Generate content based on:
                  Task: {original_task}
                  Style: {task_type}
                  Guidelines: {task_description}
                  """;

  private final ChatClient chatClient;

  public Orchestrator(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @Override
  public List<String> processUserMessage(UserMessage userMessage) {
    logger.info("Got user message: {}", userMessage);
    String userMessageText = userMessage.getText();
    OrchestratorResponse orchestratorResponse =
        chatClient
            .prompt()
            .system(SYSTEM_PROMPT)
            .user(userMessageText)
            .call()
            .entity(OrchestratorResponse.class);

    logger.info("Orchestrator response: {}", orchestratorResponse);

    List<String> workerResponses =
        orchestratorResponse.tasks().stream()
            .map(
                task ->
                    chatClient
                        .prompt()
                        .user(
                            u ->
                                u.text(WORKER_PROMPT)
                                    .param("original_task", userMessageText)
                                    .param("task_type", task.type())
                                    .param("task_description", task.description()))
                        .call()
                        .content())
            .toList();

    return workerResponses;
  }
}
