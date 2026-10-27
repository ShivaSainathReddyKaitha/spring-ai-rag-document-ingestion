package com.project.LLM.service;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class ChatBotService {
	
	private final VectorStore vectorStore;
	private final ChatClient chatClient;
	
	public ChatBotService(ChatClient chatClient, VectorStore vectorStore) {
		this.chatClient = chatClient;
		this.vectorStore = vectorStore;
	}
	
	@Value("classpath:prompts/system_prompt_template.st")
	private Resource systemPrompt;
	
	public Flux<String> respondToUser(String message, String userName) {
		
		SearchRequest searchRequest = SearchRequest.builder()
													.query(message)
													.topK(3)
													.similarityThreshold(0.5)
													.build();
		List<Document> similaritySearch = vectorStore.similaritySearch(searchRequest);
		
		List<@Nullable String> list = similaritySearch.stream().map(Document::getText).toList();
		
		return chatClient.prompt()
					.system(promptSystemSpec -> promptSystemSpec.text(systemPrompt)
							.param("documentSearchResult", list))
					.user(message)
				  .advisors(adviceSpec -> adviceSpec.param(CONVERSATION_ID, userName))
				  .stream()
				  .content();
	}

}
