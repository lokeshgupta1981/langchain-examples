package com.howtodoinjava.ai.demo;

import dev.langchain4j.agent.tool.JsonSchemaProperty;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class FunctionCallingWithToolSpecificationApplication {

  public static void main(String[] args) {
    SpringApplication.run(FunctionCallingWithToolSpecificationApplication.class);
  }

  @Value("${OPENAI_API_KEY}")
  private String OPENAI_API_KEY;

  //Uncomment to run
  //@Bean("functionCallingApplicationRunner")
  ApplicationRunner applicationRunner() {
    return args -> {

      ChatLanguageModel model = OpenAiChatModel.builder()
          .apiKey(OPENAI_API_KEY)
          .modelName(OpenAiChatModelName.GPT_3_5_TURBO)
          .build();

      ToolSpecification weatherToolSpec = ToolSpecification.builder()
          .name("getWeatherForecast")
          .description("Get the weather forecast for a location")
          .addParameter("location", JsonSchemaProperty.STRING,
              JsonSchemaProperty.description("the location to get the weather forecast for"))
          .build();

      List<ChatMessage> allMessages = new ArrayList<>();

      // 1) Ask about the weather
      UserMessage weatherQuestion = UserMessage.from("What is the weather in Paris?");
      allMessages.add(weatherQuestion);

      // 2) The model replies with a function call request
      Response<AiMessage> messageResponse = model.generate(allMessages, weatherToolSpec);
      ToolExecutionRequest toolExecutionRequest = messageResponse.content().toolExecutionRequests().get(0);
      System.out.println("Tool execution request: " + toolExecutionRequest);
      allMessages.add(messageResponse.content());

      // Here, we would call a real weather forecast service

      // 3) We send back the result of the function call
      ToolExecutionResultMessage toolExecResMsg = ToolExecutionResultMessage.from(toolExecutionRequest,
          "{\"location\":\"Paris\",\"forecast\":\"sunny\", \"temperature\": 20}");
      allMessages.add(toolExecResMsg);

      // 4) The model answers with a sentence describing the weather
      Response<AiMessage> weatherResponse = model.generate(allMessages);
      System.out.println("Answer: " + weatherResponse.content().text());
    };
  }
}
