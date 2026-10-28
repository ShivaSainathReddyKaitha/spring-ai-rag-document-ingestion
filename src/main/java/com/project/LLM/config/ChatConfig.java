package com.project.LLM.config;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ChatConfig {
	
	@Bean
	ChatMemory chatMemory(JdbcChatMemoryRepository repository) {
		return MessageWindowChatMemory.builder()
								.chatMemoryRepository(repository)
								.maxMessages(5)
								.build();
		
	}
	
	@Bean
	ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory
			) {

		Advisor messageAdvisor = MessageChatMemoryAdvisor
								.builder(chatMemory)
								.build();

		return builder
				.defaultAdvisors(List.of(messageAdvisor))
				.build();
	}
	
	

}
