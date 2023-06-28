package com.ll.gong9ri.base.appConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
public class AppConfig {
	@Getter
	private static ApplicationContext context;

	@Autowired
	public void setContext(ApplicationContext context) {
		AppConfig.context = context;
	}

	@Getter
	private static String defaultImageUploadURL;

	@Value("${custom.image.upload-dir}")
	public void setLikeablePersonFromMax(String defaultImageUploadURL) {
		AppConfig.defaultImageUploadURL = defaultImageUploadURL;
	}
}
