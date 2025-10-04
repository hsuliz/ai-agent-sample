package com.github.hsuliz.aiagentsample.domain;

import com.github.hsuliz.aiagentsample.domain.spellchecker.SpellChecker;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpellCheckerIT {

  @Autowired SpellChecker spellChecker;

  @Test
  void testProcessUserMessage() {
    final var res =
        spellChecker.processUserMessage(
            List.of(
                "Their going too the store later.",
                "I have went to the mall yesterday.",
                "He don’t like vegetables.",
                "She is more smarter than him."));
    System.out.println(res);
  }
}
