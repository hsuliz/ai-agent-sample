package com.github.hsuliz.aiagentsample.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PoetIntegrationTest {

  @Autowired Orchestrator orchestrator;

  @Test
  public void testProcessUserMessage() {
      var response = orchestrator.processPrompt("How to write email");
    System.out.println(response);
  }
}
