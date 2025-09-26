package com.github.hsuliz.aiagentsample.domain;

import org.springframework.ai.chat.messages.UserMessage;

public interface AIAgent<T> {

  public T processUserMessage(UserMessage userMessage);
}
