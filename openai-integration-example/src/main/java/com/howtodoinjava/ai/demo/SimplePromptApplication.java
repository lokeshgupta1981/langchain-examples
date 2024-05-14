package com.howtodoinjava.ai.demo;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@SpringBootApplication
public class SimplePromptApplication {

  public static void main(String[] args) {
    SpringApplication.run(SimplePromptApplication.class);
  }

  @Value("${OPENAI_API_KEY}")
  private String OPENAI_API_KEY;

  //Uncomment to run
  //@Bean("promptApplicationRunner")
  ApplicationRunner applicationRunner() {
    return args -> {

      ChatLanguageModel model = OpenAiChatModel.builder()
          .apiKey(OPENAI_API_KEY)
          .modelName(OpenAiChatModelName.GPT_3_5_TURBO)
          /*.logRequests(true)
          .logResponses(true)
          .maxRetries(3)
          .timeout(Duration.ofMillis(10000))*/
          .temperature(0.9)
          .maxTokens(100)
          .user("app-user")
          .build();

      System.out.println(model.generate("Why is the sky blue?"));

      StreamingChatLanguageModel streamingModel = OpenAiStreamingChatModel.builder()
          .apiKey(OPENAI_API_KEY)
          .modelName(OpenAiChatModelName.GPT_3_5_TURBO)
          .temperature(0.9)
          .maxTokens(100)
          .build();

      streamingModel.generate("Why is the sky blue?", new StreamingResponseHandler<AiMessage>() {
        @Override
        public void onNext(String text) {
          System.out.print(text);
        }

        @Override
        public void onComplete(Response response) {
          //System.out.print(response.finishReason().name());
        }

        @Override
        public void onError(Throwable error) {
          error.printStackTrace();
        }
      });
    };
  }
}