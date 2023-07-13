package com.ll.gong9ri.boundedContext.chatRoomParticipants.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.event.EventAfterParticipateChatRoom;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.service.ChatRoomParticipantService;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class ParticipateEventHandler {
	private final ChatRoomParticipantService chatRoomParticipantService;

	@EventListener
	public void createNewParticipant(EventAfterParticipateChatRoom event) {

		chatRoomParticipantService.createNewParticipant(event.getGroupBuyChatRoom(), event.getMember());
	}
}
