package com.howtodoinjava.ai.demo;

import com.howtodoinjava.ai.demo.service.ChatAssistant;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
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
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class);
  }

  @Value("${OPENAI_API_KEY}")
  private String OPENAI_API_KEY;

  private OpenAiChatModel chatLanguageModel;
  private StreamingChatLanguageModel streamingChatLanguageModel;

  //Uncomment to run
  //@Bean
  ApplicationRunner applicationRunner(ChatAssistant chatAssistant,
                                      ChatLanguageModel chatLanguageModel,
                                      StreamingChatLanguageModel streamingChatLanguageModel) {
    return args -> {

     /*String message1 = chatAssistant.chat("List of 5 countries based on their populations in descending order");
      System.out.println(message1);*/

    /*  String message2 = chatAssistant.query("5 famous tourist spots in UAE");
      System.out.println(message2);*/

     /* System.out.println(chatAssistant.getTime("What is the time now?"));*/

      /*String userMessageText = """
          Tell me about {{place}}.
          Write the answer briefly in form of a list.
          """;

      AiMessage userMessage = AiMessage.from(userMessageText.replace("{{place}}", "UAE"));

      AiMessage systemMessage = AiMessage.from("""
          You are a helpful AI assistant that helps people find information.
          Your name is Alexa
          Start with telling your name and quick summary of answer you are going to provide in a sentence.
          Next, you should reply to the user's request. 
          Finish with thanking the user for asking question in the end.
          """);

      Response<AiMessage> response = chatLanguageModel.generate(userMessage, systemMessage);
      System.out.println(response.content());

      streamingChatLanguageModel.generate("userMessage", new StreamingResponseHandler<AiMessage>() {

        @Override
        public void onNext(String token) {
          System.out.print(token);
        }

        @Override
        public void onComplete(Response<AiMessage> response) {
          //Do nothing
          //System.out.println(response.content());
        }

        @Override
        public void onError(Throwable error) {
          error.printStackTrace();
        }
      });*/

      /*PromptTemplate promptTemplate = PromptTemplate.from("""
            You're a friendly chef with a lot of cooking experience.
            Create a recipe for a {{dish}} with the following ingredients: \
            {{ingredients}}, and give it a name.
            """
      );

      Map<String, Object> variables = new HashMap<>();
      variables.put("dish", "dessert");
      variables.put("ingredients", "strawberries, chocolate, and whipped cream");

      Prompt prompt = promptTemplate.apply(variables);

      Response<AiMessage> response = chatLanguageModel.generate(prompt.toUserMessage());*/

    };
  }
}
