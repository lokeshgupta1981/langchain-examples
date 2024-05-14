package com.howtodoinjava.ai.demo.controller;

import com.howtodoinjava.ai.demo.service.ChatAssistant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

  private ChatAssistant chatAssistant;

  public ChatController(ChatAssistant chatAssistant){
    this.chatAssistant = chatAssistant;
  }

  @GetMapping("/chat")
  public String chat(String message) {
    return chatAssistant.chat(message);
  }

  @GetMapping("/query")
  public String query(String topic) {
    return chatAssistant.chat(topic);
  }

  @GetMapping("/get-time")
  public String getTime(@RequestParam(value = "message", defaultValue = "What is the time now?") String message) {
    return chatAssistant.getTime(message);
  }
}
