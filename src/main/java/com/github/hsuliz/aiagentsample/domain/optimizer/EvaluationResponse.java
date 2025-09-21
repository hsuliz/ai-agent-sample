package com.github.hsuliz.aiagentsample.domain.optimizer;

record EvaluationResponse(Evaluation evaluation, String feedback) {

  enum Evaluation {
    PASS,
    NEEDS_IMPROVEMENT,
    FAIL
  }
}
