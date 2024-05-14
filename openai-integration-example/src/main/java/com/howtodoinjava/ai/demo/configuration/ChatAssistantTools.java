package com.howtodoinjava.ai.demo.configuration;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

//@Component
public class ChatAssistantTools {

  @Tool("Get Current Time")
  String currentTime() {
    return LocalTime.now().toString();
  }
}
