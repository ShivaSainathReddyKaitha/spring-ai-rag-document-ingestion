package com.project.LLM.config;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class RAGConfigPDF {
	
	private final VectorStore vectorStore;
	
	@Value("classpath:Sample_Employment_Handbook.pdf")
	Resource pdf;

	public RAGConfigPDF(VectorStore vectorStore) {
		this.vectorStore = vectorStore;
	}
	
	@PostConstruct
	public void loadData() {
		TikaDocumentReader reader = new TikaDocumentReader(pdf);
		List<Document> list = reader.get();
		TextSplitter textSplitter = TokenTextSplitter.builder()
									.withChunkSize(30)
									.withMaxNumChunks(1000)
									.build();
		vectorStore.add(textSplitter.split(list));
	}
	

}
