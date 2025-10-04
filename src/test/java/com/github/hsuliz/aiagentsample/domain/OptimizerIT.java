package com.github.hsuliz.aiagentsample.domain;

import com.github.hsuliz.aiagentsample.domain.optimizer.Optimizer;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OptimizerIT {

  @Autowired Optimizer optimizer;

  @Test
  void testProcessUserMessage() {
    final var res = optimizer.processUserMessage(new UserMessage("Calculate 2 + 2 and then ad 84"));
    System.out.println(res);
  }
}
