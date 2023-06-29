package com.ll.gong9ri.base.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
	// private final CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.formLogin(
				formLogin -> formLogin
					.loginPage("/member/login")
				// .successHandler(customOAuth2AuthenticationSuccessHandler)
			)
			.oauth2Login(
				oauth2Login -> oauth2Login
					.loginPage("/member/login")
				// .successHandler(customOAuth2AuthenticationSuccessHandler)
			)
			.logout(
				logout -> logout
					.logoutUrl("/member/logout")
			);

		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}