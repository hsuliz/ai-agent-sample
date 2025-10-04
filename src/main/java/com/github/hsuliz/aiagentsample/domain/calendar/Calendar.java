package com.github.hsuliz.aiagentsample.domain.calendar;

import com.github.hsuliz.aiagentsample.domain.AIAgent;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

@Component
public class Calendar implements AIAgent<CalendarEvent> {

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
  private final Logger logger = LoggerFactory.getLogger(Calendar.class);
  private final ChatClient chatClient;

  public Calendar(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  public CalendarEvent processUserMessage(UserMessage userMessage) {
    logger.info("Got user message: {}", userMessage);

    String currentDate = "Current date:" + LocalDate.now();
    SystemMessage instructionWithCurrentDate = new SystemMessage(INSTRUCTION + currentDate);
    Prompt prompt = new Prompt(instructionWithCurrentDate, userMessage);
    CalendarEventAIResponse response =
        chatClient.prompt(prompt).call().entity(CalendarEventAIResponse.class);

    if (response == null) throw new RuntimeException("OrchestratorResponse is null");
    if (!response.parsed()) throw new RuntimeException("Can't parse");

    logger.info(String.valueOf(response.action()));

    switch (response.action()) {
      case ADD, MODIFY, DELETE -> {
        logger.info("Created response: {}", response);
        return new CalendarEvent(response);
      }
      default -> throw new IllegalStateException("Unexpected value: " + response.action());
    }
  }
}
