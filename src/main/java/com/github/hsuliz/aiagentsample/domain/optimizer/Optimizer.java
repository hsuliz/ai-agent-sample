package com.github.hsuliz.aiagentsample.domain.optimizer;

import java.util.ArrayList;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class Optimizer {

  private final ChatClient chatClient;

  public Optimizer(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  private static final String SYSTEM_PROMPT =
      """
            Your goal is to complete the task based on the input. If there are feedback
            from your previous generations, you should reflect on them to improve your solution.
            Respond with EXACTLY JSON format:
            {"thoughts":"...","response":"..."}
            """;

  private static final String EVALUATOR_PROMPT =
      """
            Evaluate task and apply best practises as you as you understood.
            Respond with EXACTLY JSON format:
            {"evaluation":"PASS, NEEDS_IMPROVEMENT, or FAIL", "feedback":"Your feedback here"}
            The evaluation field must be one of: "PASS", "NEEDS_IMPROVEMENT", "FAIL"
            Use "PASS" only if all criteria are met with no improvements needed.
            """;

  public record RefinedResponse(String solution, List<Generation> chainOfThought) {}

  public RefinedResponse processPrompt(String task, Integer steps) {
    List<String> memory = new ArrayList<>();
    List<Generation> chainOfThought = new ArrayList<>();

    return contextLoop(task, "", memory, chainOfThought);
  }

  private RefinedResponse contextLoop(
      String task, String context, List<String> memory, List<Generation> chainOfThought) {
    final var generationResponse =
        chatClient
            .prompt()
            .user(
                it ->
                    it.text("{prompt}\n{context}\nTask: {task}")
                        .param("prompt", SYSTEM_PROMPT)
                        .param("context", context)
                        .param("task", task))
            .call()
            .entity(Generation.class);

    if (generationResponse == null) {
      throw new RuntimeException("Generation is null");
    }

    System.out.printf(
        "\n=== GENERATOR OUTPUT ===\nTHOUGHTS: %s\n\nRESPONSE:\n %s\n%n",
        generationResponse.thoughts(), generationResponse.response());

    memory.add(generationResponse.response());
    chainOfThought.add(generationResponse);

    final var evaluationResponse =
        chatClient
            .prompt()
            .user(
                u ->
                    u.text("{prompt}\nOriginal task: {task}\nContent to evaluate: {content}")
                        .param("prompt", EVALUATOR_PROMPT)
                        .param("task", task)
                        .param("content", generationResponse.response()))
            .call()
            .entity(EvaluationResponse.class);

    if (evaluationResponse == null) {
      throw new RuntimeException("Evaluation response is null");
    }

      System.out.printf("\n=== EVALUATOR OUTPUT ===\nEVALUATION: %s\n\nFEEDBACK: %s\n%n",
              evaluationResponse.evaluation(), evaluationResponse.feedback());

    if (evaluationResponse.evaluation().equals(EvaluationResponse.Evaluation.PASS)) {
      return new RefinedResponse(generationResponse.response(), chainOfThought);
    }

    final var updatedContext = new StringBuilder();
    updatedContext.append("Previous attempts:");
    memory.forEach(
        it -> {
          updatedContext.append("\n- ");
          updatedContext.append(it);
        });

    updatedContext.append("\nFeedback: ").append(evaluationResponse.feedback());

    return contextLoop(task, updatedContext.toString(), memory, chainOfThought);
  }
}
