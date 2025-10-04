package com.github.hsuliz.aiagentsample.domain.calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;

public record CalendarEventAIResponse(
    Boolean parsed,
    Action action,
    String title,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") LocalDate date,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") LocalTime time) {}
