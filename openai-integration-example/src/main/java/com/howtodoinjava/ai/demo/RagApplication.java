package com.howtodoinjava.ai.demo;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModelName;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.FileInputStream;
import java.util.List;

@SpringBootApplication
public class RagApplication {

  public static void main(String[] args) {
    SpringApplication.run(RagApplication.class);
  }

  @Value("${OPENAI_API_KEY}")
  private String OPENAI_API_KEY;

  interface LlmAgent {
    String ask(String question);
  }

  @Bean("ragApplicationRunner")
  ApplicationRunner applicationRunner() {
    return args -> {

      // 1. Document ingestion
      ApachePdfBoxDocumentParser pdfParser = new ApachePdfBoxDocumentParser();
      Document document = pdfParser.parse(new FileInputStream(
          "c:/temp/spring-boot-reference-part-2.pdf"));

      OpenAiEmbeddingModel embeddingModel = OpenAiEmbeddingModel.builder()
          .apiKey(OPENAI_API_KEY)
          .modelName(OpenAiEmbeddingModelName.TEXT_EMBEDDING_3_SMALL)
          .logRequests(true)
          .logResponses(true)
          .build();

      InMemoryEmbeddingStore<TextSegment> embeddingStore =
          new InMemoryEmbeddingStore<>();

      EmbeddingStoreIngestor storeIngestor = EmbeddingStoreIngestor.builder()
          .documentSplitter(DocumentSplitters.recursive(500, 100))
          .embeddingModel(embeddingModel)
          .embeddingStore(embeddingStore)
          .build();
      storeIngestor.ingest(document);

      // 2. Asking questions
      ChatLanguageModel model = OpenAiChatModel.builder()
          .apiKey(OPENAI_API_KEY)
          .modelName(OpenAiChatModelName.GPT_3_5_TURBO)
          .logRequests(true)
          .logResponses(true)
          .build();

      EmbeddingStoreContentRetriever retriever =
          new EmbeddingStoreContentRetriever(embeddingStore, embeddingModel);

      LlmAgent llmAgent = AiServices.builder(LlmAgent.class)
          .chatLanguageModel(model)
          .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
          .contentRetriever(retriever)
          .build();

      List.of(
          "Explain Spring Boot?",
          "Provide default settings for Spring Boot DataSource"
      ).forEach(query ->
          System.out.printf("%n=== %s === %n%n %s %n%n", query, llmAgent.ask(query)));
    };
  }
}
