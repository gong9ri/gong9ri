package com.ll.gong9ri.boundedContext.fcm.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.ll.gong9ri.base.event.NoticeUpdatedEvent;
import com.ll.gong9ri.boundedContext.fcm.service.FcmService;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class NoticeEventHandler {

	private final FcmService fcmService;

	@EventListener
	public void sendMessage(NoticeUpdatedEvent event) throws FirebaseMessagingException {
		fcmService.sendMessageToClients(event.getTokens(), event.getMessageHead(), event.getMessageContent());
	}
}
