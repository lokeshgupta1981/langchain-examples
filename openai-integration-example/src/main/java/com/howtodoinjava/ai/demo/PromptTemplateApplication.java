package com.howtodoinjava.ai.demo;

import dev.langchain4j.data.message.AiMessage;
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

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class PromptTemplateApplication {

  public static void main(String[] args) {
    SpringApplication.run(PromptTemplateApplication.class);
  }

  @Value("${OPENAI_API_KEY}")
  private String OPENAI_API_KEY;

  //Uncomment to run
  //@Bean("promptTemplateApplicationRunner")
  ApplicationRunner applicationRunner() {
    return args -> {

      ChatLanguageModel model = OpenAiChatModel.builder()
          .apiKey(OPENAI_API_KEY)
          .modelName(OpenAiChatModelName.GPT_3_5_TURBO)
          .build();

      PromptTemplate promptTemplate = PromptTemplate.from("""
            You're a friendly chef with a lot of cooking experience.
            Create a recipe for a {{dish}} with the following ingredients: \
            {{ingredients}}, and give it a name.
            """
      );

      Map<String, Object> variables = new HashMap<>();
      variables.put("dish", "dessert");
      variables.put("ingredients", "strawberries, chocolate, and whipped cream");

      Prompt prompt = promptTemplate.apply(variables);

      Response<AiMessage> response = model.generate(prompt.toUserMessage());

      System.out.println(response.content().text());
    };
  }
}
