package com.ll.gong9ri.boundedContext.fcm.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.fcm.dto.TokenDTO;
import com.ll.gong9ri.boundedContext.fcm.token.Token;
import com.ll.gong9ri.boundedContext.member.entity.Member;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class FcmServiceTest {

	@Autowired
	private FcmService fcmService;

	@Test
	@DisplayName("saveToken")
	public void saveToken() throws Exception {
		//given
		TokenDTO tokenDTO = new TokenDTO("testTokenString");
		Member member = new Member().toBuilder()
			.build();

		//when
		RsData<Token> tokenRsData = fcmService.saveToken(tokenDTO, member);

		//then
		assertEquals("S-1", tokenRsData.getResultCode());
	}
}