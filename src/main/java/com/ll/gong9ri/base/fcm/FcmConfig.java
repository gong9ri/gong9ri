package com.ll.gong9ri.base.fcm;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

@Component
public class FcmConfig {
	private static final String path = "gong9ri-e7e80-firebaseadminsdk.json";

	@PostConstruct
	public void init() {
		try {
			FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(new ClassPathResource(path).getInputStream()))
				.build();

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}