package com.github.hsuliz.aiagentsample.domain;

import com.github.hsuliz.aiagentsample.domain.dto.CalendarEventResponse;
import java.time.LocalDate;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

@Component
public class Calendar {

  private final ChatClient chatClient;

  public Calendar(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  private static final String INSTRUCTION =
      """
     You are a smart AI calendar planner.

     Based on the user's request, try to parse the information. \s
     If you successfully parse the request, set "parsed" to true; otherwise, set it to false.

     Respond ONLY in the following JSON format (no extra text):
     IF PARSED IF FALSE, DO NOT RETURN ANY ADDITIONAL DATA

     \\{
       "parsed": true or false,
       "action": "add", "delete", or "modify",
       "title": "...",
       "date": "DD-MM-YYYY",
       "time": "HH:MM"
     \\}
     """;

  public void processUserMessage(UserMessage userMessage) {
    var currentDate = "Current date:" + LocalDate.now();
    var instructionWithCurrentDate = new SystemMessage(INSTRUCTION + currentDate);
    Prompt prompt = new Prompt(instructionWithCurrentDate, userMessage);
    var promptCall = chatClient.prompt(prompt).call();
    var response = promptCall.entity(CalendarEventResponse.class);
    if (response == null) {
      throw new RuntimeException("Response is null");
    }

    if (!response.parsed()) {
      throw new RuntimeException("Can't parse");
    }

    switch (response.action()) {
      case ADD -> System.out.println("Add");
      case DELETE -> System.out.println("Delete");
      case MODIFY -> System.out.println("Modify");
    }
  }
}
