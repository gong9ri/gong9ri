package com.ll.gong9ri.boundedContext.fcm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.ll.gong9ri.base.event.EventAfterUpdateToken;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.fcm.dto.TokenDTO;
import com.ll.gong9ri.boundedContext.fcm.repository.TokenRepository;
import com.ll.gong9ri.boundedContext.fcm.token.Token;
import com.ll.gong9ri.boundedContext.member.entity.Member;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {
	private final TokenRepository tokenRepository;
	private final ApplicationEventPublisher publisher;

	@Transactional
	public RsData<Token> saveToken(TokenDTO tokenDTO, Member member) {
		Optional<Token> existingToken = tokenRepository.findByMemberId(member.getId());
		if (existingToken.isPresent()) {
			return update(tokenDTO, existingToken.get(), member);
		}

		Token newToken = Token.builder()
			.tokenString(tokenDTO.getTokenString())
			.member(member)
			.build();

		tokenRepository.save(newToken);

		return RsData.of("S-1", "token 이 저장되었습니다.", newToken);
	}

	private RsData<Token> update(final TokenDTO tokenDTO, final Token existingToken, Member member) {
		final Token token = existingToken.toBuilder()
			.tokenString(tokenDTO.getTokenString())
			.build();

		tokenRepository.save(token);

		publisher.publishEvent(new EventAfterUpdateToken(token.getTokenString(), member.getId()));

		return RsData.of("S-2", "token 이 저장되었습니다.", token);
	}

	public void sendMessageToClients(
		final List<String> clientTokens,
		final String messageHead,
		final String messageContent
	) {
		final MulticastMessage message = MulticastMessage.builder()
			.setNotification(Notification.builder()
				.setTitle(messageHead)
				.setBody(messageContent)
				.build())
			.addAllTokens(clientTokens)
			.build();

		try {
			// send Message and get response
			// BatchResponse response = : am i need?
			FirebaseMessaging.getInstance().sendEachForMulticast(message);
		} catch (FirebaseMessagingException e) {
			// ignore fcm exception
			e.printStackTrace();
		}
	}

	public Optional<Token> findByMemberId(Long id) {
		return tokenRepository.findByMemberId(id);
	}
}
