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

  record Person(String name, int age) {}

  interface PersonExtractor {
    @UserMessage("""
            Extract the name and age of the person described below.
            Return a JSON document with a "name" and an "age" property, \
            following this structure: {"name": "John Doe", "age": 34}
            Return only JSON, without any markdown markup surrounding it.
            Here is the document describing the person:
            ---
            {{it}}
            ---
            JSON:
            """)
    Person extractPerson(String text);
  }

  //Uncomment to run
  //@Bean("structuredResponseApplicationRunner")
  ApplicationRunner applicationRunner() {
    return args -> {

      ChatLanguageModel model = OpenAiChatModel.builder()
          .apiKey(OPENAI_API_KEY)
          .modelName(OpenAiChatModelName.GPT_3_5_TURBO)
          .build();

      PersonExtractor extractor = AiServices.create(PersonExtractor.class, model);

      Person person = extractor.extractPerson("""
            Anna is a 23 year old artist based in Brooklyn, New York. She was born and 
            raised in the suburbs of Chicago, where she developed a love for art at a 
            young age. She attended the School of the Art Institute of Chicago, where 
            she studied painting and drawing. After graduating, she moved to New York 
            City to pursue her art career. Anna's work is inspired by her personal 
            experiences and observations of the world around her. She often uses bright 
            colors and bold lines to create vibrant and energetic paintings. Her work 
            has been exhibited in galleries and museums in New York City and Chicago.    
            """
      );

      System.out.println(person.name());  // Anna
      System.out.println(person.age());   // 23
    };
  }
}
