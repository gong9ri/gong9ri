package com.ll.gong9ri.base.appConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
public class AppConfig {
	@Getter
	private static String defaultImageUploadURL;

	@Value("${custom.image.upload-dir}")
	public void setDefaultImageUploadURL(String defaultImageUploadURL) {
		AppConfig.defaultImageUploadURL = defaultImageUploadURL;
	}

	@Getter
	private static String baseUrl;

	@Value("${custom.baseUrl}")
	public void setBaseUrl(String baseUrl) {
		AppConfig.baseUrl = baseUrl;
	}
}
