package com.howtodoinjava.ai.demo;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StructuredResponseApplication {

  public static void main(String[] args) {
    SpringApplication.run(StructuredResponseApplication.class);
  }

  @Value("${OPENAI_API_KEY}")
  private String OPENAI_API_KEY;

  record Person(String name, int age, String country, String city) {
  }

  interface PersonExtractor {
    @UserMessage("""
        Extract the name, age. city and country of the person described below.
        Return only JSON, without any markdown markup surrounding it.
        Here is the document describing the person:
        ---
        {{it}}
        """)
    Person extract(String text);
  }

  //Uncomment to run
  //@Bean("structuredResponseApplicationRunner")
  ApplicationRunner applicationRunner() {
    return args -> {

      ChatLanguageModel model = OpenAiChatModel.builder()
          .apiKey(OPENAI_API_KEY)
          .modelName(OpenAiChatModelName.GPT_3_5_TURBO)
          .logRequests(true)
          .logResponses(true)
          .build();

      PersonExtractor personExtractor = AiServices.create(PersonExtractor.class, model);

      String inputText = """
          Charles Brown, aged 56, resides in the United Kingdom. Originally 
          from a small town in Devon Charles developed a passion for history 
          and archaeology from an early age, which led him to pursue a career 
          as an archaeologist specializing in medieval European history. 
          He completed his education at the University of Oxford, where 
          he earned a degree in Archaeology and History.    
          """;

      Person person = personExtractor.extract(inputText);

      System.out.println(person);
    };
  }
}
