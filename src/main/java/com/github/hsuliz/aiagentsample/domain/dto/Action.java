package com.github.hsuliz.aiagentsample.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Action {
  ADD,
  DELETE,
  MODIFY;

  @JsonCreator
  public static Action fromString(String key) {
    return key == null ? null : Action.valueOf(key.toUpperCase());
  }

  @JsonValue
  public String toValue() {
    return this.name().toLowerCase();
  }
}
