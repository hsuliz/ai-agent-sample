package com.github.hsuliz.aiagentsample.handler;

import com.github.hsuliz.aiagentsample.domain.SpellChecker;
import java.util.List;
import org.springframework.http.ResponseEntity;
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
}
