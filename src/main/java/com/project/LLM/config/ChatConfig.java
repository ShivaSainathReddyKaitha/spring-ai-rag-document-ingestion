package com.project.LLM.config;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
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
	ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory,
			RetrievalAugmentationAdvisor retrievalAugmentationAdvisor,
			@Value("classpath:prompts/system_prompt_template.st") Resource systemPrompt
			) {

		Advisor messageAdvisor = MessageChatMemoryAdvisor
								.builder(chatMemory)
								.build();

		return builder
				.defaultAdvisors(List.of(messageAdvisor, retrievalAugmentationAdvisor))
				.defaultSystem(systemPrompt)
				.build();
	}
	
	@Bean
	RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore) {
		return RetrievalAugmentationAdvisor.builder()
				.documentRetriever(VectorStoreDocumentRetriever.builder()
									.vectorStore(vectorStore)
									.topK(3)
									.similarityThreshold(0.5)
									.build())
				.build();
	}
	
	

}
