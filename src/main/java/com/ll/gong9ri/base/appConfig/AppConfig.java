package com.ll.gong9ri.base.appConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.Getter;

@Configuration
public class AppConfig {
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper().registerModule(new JavaTimeModule());
	}

	@Getter
	private static ApplicationContext context;

	@Autowired
	public void setContext(ApplicationContext context) {
		AppConfig.context = context;
	}

	@Getter
	private static String defaultImageUploadURL;

	@Value("${custom.image.upload-dir}")
	public void setDefaultImageUploadURL(String defaultImageUploadURL) {
		AppConfig.defaultImageUploadURL = defaultImageUploadURL;
	}

	@Getter
	private static String baseUrl;

	@Value("${custom.site.baseUrl}")
	public void setBaseUrl(String baseUrl) {
		AppConfig.baseUrl = baseUrl;
	}
}
