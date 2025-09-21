package com.github.hsuliz.aiagentsample.domain.orchestrator;

import java.util.List;

record OrchestratorResponse(String analysis, List<Task> tasks) {}
