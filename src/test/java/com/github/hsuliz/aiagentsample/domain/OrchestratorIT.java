package com.github.hsuliz.aiagentsample.domain;

import com.github.hsuliz.aiagentsample.domain.orchestrator.Orchestrator;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrchestratorIT {

  @Autowired Orchestrator orchestrator;

  @Test
  public void testProcessUserMessage() {
    var res = orchestrator.processUserMessage(new UserMessage("How to write email"));
    System.out.println(res);
  }
}
