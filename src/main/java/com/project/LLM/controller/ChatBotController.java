package com.project.LLM.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.LLM.service.ChatBotService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class ChatBotController {
	
	private final ChatBotService service;
	
	public ChatBotController(ChatBotService service) {
		this.service = service;
	}
	
	@GetMapping("/chat")
	public Flux<String> generateResponse(@RequestParam String message, @RequestHeader("username") String username) {
		return service.respondToUser(message, username);
	}

}
