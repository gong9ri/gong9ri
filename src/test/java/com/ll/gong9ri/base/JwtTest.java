package com.ll.gong9ri.base;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.jwt.JwtProvider;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class JwtTest {
	@Autowired
	private JwtProvider jwtProvider;

	@Test
	@DisplayName("generate Access Token ")
	void genAccessTokenTest() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", 1L);
		claims.put("username", "admin");

		// 지금으로부터 5시간의 유효기간을 가지는 토큰을 생성
		String accessToken = jwtProvider.genToken(claims, 60 * 60 * 5);

		System.out.println("accessToken : " + accessToken);

		assertThat(accessToken).isNotNull();
	}

	@Test
	@DisplayName("get Claims from accessToken")
	void getClaimsTest() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", 1L);
		claims.put("username", "admin");

		String accessToken = jwtProvider.genToken(claims, 60 * 60 * 5);

		System.out.println("accessToken : " + accessToken);

		assertThat(jwtProvider.verify(accessToken)).isTrue();

		Map<String, Object> claimsFromToken = jwtProvider.getClaims(accessToken);
		System.out.println("claimsFromToken : " + claimsFromToken);
	}

	@Test
	@DisplayName("check Expiration from accessToken")
	void tokenExpireTest() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", 1L);
		claims.put("username", "admin");

		String accessToken = jwtProvider.genToken(claims, -1);

		System.out.println("accessToken : " + accessToken);

		assertThat(jwtProvider.verify(accessToken)).isFalse();
	}
}
