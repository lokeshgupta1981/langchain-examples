package com.howtodoinjava.ai.demo.configuration;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatAssistantConfiguration {

  @Bean
  ChatMemory chatMemory() {
    return MessageWindowChatMemory.withMaxMessages(10);
  }
}
