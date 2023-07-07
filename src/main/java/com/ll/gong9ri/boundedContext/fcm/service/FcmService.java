package com.ll.gong9ri.boundedContext.fcm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.fcm.dto.TokenDTO;
import com.ll.gong9ri.boundedContext.fcm.repository.TokenRepository;
import com.ll.gong9ri.boundedContext.fcm.token.Token;
import com.ll.gong9ri.boundedContext.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FcmService {

	private final TokenRepository tokenRepository;

	@Transactional
	public RsData<Token> saveToken(TokenDTO token, Member member) {

		Token newToken = Token.builder()
			.tokenString(token.getTokenString())
			.member(member)
			.build();

		tokenRepository.save(newToken);

		return RsData.of("S-1", "token 이 저장되었습니다.", newToken);
	}
}
