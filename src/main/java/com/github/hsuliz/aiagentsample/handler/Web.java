package com.github.hsuliz.aiagentsample.handler;

import com.github.hsuliz.aiagentsample.domain.spellchecker.SpellChecker;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Web {

  private final SpellChecker spellChecker;

  public Web(SpellChecker spellChecker) {
    this.spellChecker = spellChecker;
  }

  @PostMapping("/spellcheck")
  public ResponseEntity<List<String>> spellcheck(@RequestBody List<String> texts) {
    var res = spellChecker.check(texts);
    return ResponseEntity.ok(res);
  }

  @GetMapping("/troubleshoot")
  public ResponseEntity<String> troubleshoot(HttpServletRequest request) {
    System.out.println(request);
    return ResponseEntity.ok("");
  }
}
