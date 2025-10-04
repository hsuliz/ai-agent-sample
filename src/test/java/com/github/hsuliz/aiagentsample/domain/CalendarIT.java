package com.github.hsuliz.aiagentsample.domain;

import com.github.hsuliz.aiagentsample.domain.calendar.Calendar;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CalendarIT {

  @Autowired Calendar calendar;

  @Test
  public void testProcessUserMessage() {
    final var res =
        calendar.processUserMessage(new UserMessage("Tomorrow i have medical meeting at 15:30"));
    System.out.println(res);
  }
}
