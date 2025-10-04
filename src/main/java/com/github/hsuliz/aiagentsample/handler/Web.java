package com.github.hsuliz.aiagentsample.handler;

import com.github.hsuliz.aiagentsample.domain.calendar.Calendar;
import com.github.hsuliz.aiagentsample.domain.calendar.CalendarEvent;
import com.github.hsuliz.aiagentsample.domain.optimizer.Optimizer;
import com.github.hsuliz.aiagentsample.domain.orchestrator.Orchestrator;
import com.github.hsuliz.aiagentsample.domain.spellchecker.SpellChecker;
import java.util.List;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Web {

  private final Calendar calendar;
  private final Optimizer optimizer;
  private final Orchestrator orchestrator;
  private final SpellChecker spellChecker;

  public Web(
      Calendar calendar,
      Optimizer optimizer,
      Orchestrator orchestrator,
      SpellChecker spellChecker) {
    this.calendar = calendar;
    this.optimizer = optimizer;
    this.orchestrator = orchestrator;
    this.spellChecker = spellChecker;
  }

  @PostMapping("/calendar")
  public ResponseEntity<CalendarEvent> calendar(@RequestBody String userMessage) {
    final var res = calendar.processUserMessage(new UserMessage(userMessage));
    return ResponseEntity.ok(res);
  }

  @PostMapping("/optimizer")
  public ResponseEntity<String> optimizer(@RequestBody String userMessage) {
    final var res = optimizer.processUserMessage(new UserMessage(userMessage));
    return ResponseEntity.ok(res);
  }

  @PostMapping("/orchestrator")
  public ResponseEntity<List<String>> spellcheck(@RequestBody String userMessage) {
    final var res = orchestrator.processUserMessage(new UserMessage(userMessage));
    return ResponseEntity.ok(res);
  }

  @PostMapping("/spellchecker")
  public ResponseEntity<List<String>> spellchecker(@RequestBody List<String> userMessages) {
    final var res = spellChecker.processUserMessage(userMessages);
    return ResponseEntity.ok(res);
  }
}
