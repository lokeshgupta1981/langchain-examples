package com.howtodoinjava.ai.demo;

import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.language.LanguageModel;
import dev.langchain4j.model.openai.*;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class MultiModelApplication {

  static final String CAT_IMAGE_URL =
      "https://upload.wikimedia.org/wikipedia/" +
          "commons/b/b6/Felis_catus-cat_on_snow.jpg";

  public static void main(String[] args) {
    SpringApplication.run(MultiModelApplication.class);
  }

  @Value("${OPENAI_API_KEY}")
  private String OPENAI_API_KEY;

  //Uncomment to run
  //@Bean("multiModelApplicationRunner")
  ApplicationRunner applicationRunner() {
    return args -> {
      ChatLanguageModel model = OpenAiChatModel.builder()
          .apiKey(OPENAI_API_KEY)
          .modelName(OpenAiChatModelName.GPT_4_VISION_PREVIEW)
          .logRequests(true)
          .logResponses(true)
          .maxRetries(1)
          .build();

      ChatMessage chatMessage = new UserMessage(
          "app-user",
          List.of(
              TextContent.from("Describe the picture in hindi"),
              ImageContent.from(CAT_IMAGE_URL))
      );

      Response<AiMessage> response = model.generate(chatMessage);
      System.out.println(response.content().text());
    };
  }
}
