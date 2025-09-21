package com.github.hsuliz.aiagentsample.domain;

import com.github.hsuliz.aiagentsample.domain.optimizer.Optimizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OptimizerTest {

    @Autowired
    Optimizer optimizer;

    @Test
    void testOptimize() {
        optimizer.processPrompt("Write proffesinal email for work application", 4);
    }
}
