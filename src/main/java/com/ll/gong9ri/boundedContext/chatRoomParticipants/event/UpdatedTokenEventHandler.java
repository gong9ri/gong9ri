package com.ll.gong9ri.boundedContext.chatRoomParticipants.event;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.event.EventAfterUpdateToken;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.entity.ChatRoomParticipant;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.service.ChatRoomParticipantService;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class UpdatedTokenEventHandler {
	private final ChatRoomParticipantService chatRoomParticipantService;

	@EventListener
	public void EventHandle(EventAfterUpdateToken event) {

		List<ChatRoomParticipant> participantList = chatRoomParticipantService.findAllByMemberId(event.getMemberId());

		String tokenString = event.getTokenString();

		participantList.forEach(p -> chatRoomParticipantService.updateToken(p, tokenString));
	}
}
