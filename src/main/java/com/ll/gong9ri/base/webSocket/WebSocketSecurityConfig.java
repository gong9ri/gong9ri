package com.ll.gong9ri.base.webSocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

import com.ll.gong9ri.boundedContext.member.entity.AuthLevel;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {

	@Bean
	public AuthorizationManager<Message<?>> messageAuthorizationManager(
		MessageMatcherDelegatingAuthorizationManager.Builder messages) {
		return messages
			.anyMessage().hasAuthority(AuthLevel.MEMBER.getValue())
			.build();
	}
}
