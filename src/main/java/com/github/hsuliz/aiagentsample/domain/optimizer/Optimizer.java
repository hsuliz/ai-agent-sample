package com.github.hsuliz.aiagentsample.domain.optimizer;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class Optimizer {

  private final Logger logger = LoggerFactory.getLogger(Optimizer.class);

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

  public record RefinedResponse(String solution, List<GenerationResponse> chainOfThought) {}

  public RefinedResponse processPrompt(String task, Integer steps) {
    String context = "";
    List<String> memory = new ArrayList<>();
    List<GenerationResponse> chainOfThought = new ArrayList<>();

    for (int i = 0; i < steps; i++) {
      logger.info("iteration number: {}", i);
      String finalContext = context;
      GenerationResponse generationResponse =
          chatClient
              .prompt()
              .user(
                  it -> {
                    it.text("{prompt}\n{context}\nTask: {task}")
                        .param("prompt", SYSTEM_PROMPT)
                        .param("context", finalContext)
                        .param("task", task);
                  })
              .call()
              .entity(GenerationResponse.class);

      if (generationResponse == null) {
        throw new RuntimeException("GenerationResponse is null");
      }

      logger.info("thoughts {}", generationResponse.thoughts());
      logger.info("response {}", generationResponse.response());

      memory.add(generationResponse.response());
      chainOfThought.add(generationResponse);

      EvaluationResponse evaluationResponse =
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

      logger.info("evaluation {}", evaluationResponse.evaluation());
      logger.info("feedback {}", evaluationResponse.feedback());

      if (evaluationResponse.evaluation().equals(EvaluationResponse.Evaluation.PASS)) {
        return new RefinedResponse(generationResponse.response(), chainOfThought);
      }

      StringBuilder updatedContext = new StringBuilder();
      updatedContext.append("Previous attempts:");
      memory.forEach(
          it -> {
            updatedContext.append("\n- ");
            updatedContext.append(it);
          });

      updatedContext.append("\nFeedback: ").append(evaluationResponse.feedback());
      context = updatedContext.toString();
    }

    return null;
  }
}
