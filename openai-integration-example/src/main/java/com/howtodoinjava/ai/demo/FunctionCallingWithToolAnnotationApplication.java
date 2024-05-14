package com.howtodoinjava.ai.demo;

import dev.langchain4j.agent.tool.*;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class FunctionCallingWithToolAnnotationApplication {

  public static void main(String[] args) {
    SpringApplication.run(FunctionCallingWithToolAnnotationApplication.class);
  }

  @Value("${OPENAI_API_KEY}")
  private String OPENAI_API_KEY;

  record WeatherForecast(String location, String forecast, int temperature) {}

  static class WeatherForecastService {
    @Tool("Get the weather forecast for a location")
    WeatherForecast getForecast(@P("Location to get the forecast for") String location) {
      if (location.equals("Paris")) {
        return new WeatherForecast("Paris", "Sunny", 20);
      } else if (location.equals("London")) {
        return new WeatherForecast("London", "Rainy", 15);
      } else {
        return new WeatherForecast("Unknown", "Unknown", 0);
      }
    }
  }

  interface WeatherAssistant {
    String chat(String userMessage);
  }

  //Uncomment to run
  //@Bean("functionCallingApplication")
  ApplicationRunner applicationRunner() {
    return args -> {

      ChatLanguageModel model = OpenAiChatModel.builder()
          .apiKey(OPENAI_API_KEY)
          .modelName(OpenAiChatModelName.GPT_3_5_TURBO)
          .build();

      WeatherForecastService weatherForecastService = new WeatherForecastService();

      WeatherAssistant assistant = AiServices.builder(WeatherAssistant.class)
          .chatLanguageModel(model)
          .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
          .tools(weatherForecastService)
          .build();

      System.out.println(assistant.chat("What is the weather in Paris?"));
    };
  }
}
