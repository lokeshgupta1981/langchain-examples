package com.howtodoinjava.ai.demo.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface ChatAssistant {

  @SystemMessage("""
        You are a helpful AI assistant that helps people find information.
        Your name is Alexa
        Start with telling your name and quick summary of answer you are going to provide in a sentence.
        Next, you should reply to the user's request. 
        Finish with thanking the user for asking question in the end.
        """)
  String chat(String userMessage);

  @UserMessage("""
        Tell me about {{topic}}.
        Write the answer briefly in form of a list.
        """)
  String query(@V("topic") String topic);

  @SystemMessage("You are a polite assistant")
  String getTime(String userMessage);
}
