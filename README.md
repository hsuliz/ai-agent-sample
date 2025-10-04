# ai-agent-sample

# Abstract

AI integration with Spring Boot with 4 patterns.

1. [Calendar](src/main/java/com/github/hsuliz/aiagentsample/domain/calendar/Calendar.java) \
   Transforms request to specific JSON which represents event in calendar.
2. [Optimizer](src/main/java/com/github/hsuliz/aiagentsample/domain/optimizer/Optimizer.java) \
   Optimize strategy used to process same request with feedback to achieve the best response.
3. [Orchestrator](src/main/java/com/github/hsuliz/aiagentsample/domain/orchestrator/Orchestrator.java) \
   Orchestrator strategy used. Request is divided into 3 distinct approaches.
4. [SpellChecker](src/main/java/com/github/hsuliz/aiagentsample/domain/spellchecker/SpellChecker.java) \
   Spellchecks given request. Request is list. Each element is processed in thread.

# Run

1. In [application.properties](src/main/resources/application.properties) you should write pass values:

   |                                       |                        |
                      |---------------------------------------|------------------------|
   | `spring.ai.openai.api-key`            | API key                |
   | `spring.ai.openai.chat.base-url`      | URL where model exists |
   | `spring.ai.openai.chat.options.model` | Model name             |

   Default values represents local hosted model in Docker.

2. Execute
    ```shell
    gradle bootRun
    ```
   Swagger docs located under this path: `/swagger-ui.html`