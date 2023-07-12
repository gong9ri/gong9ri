package com.ll.gong9ri.boundedContext.chatRoomParticipants.event;

import java.util.Optional;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.event.EventAfterNewChatReceived;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.entity.ChatRoomParticipant;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.service.ChatRoomParticipantService;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class OffsetEventHandler {
	private final ChatRoomParticipantService chatRoomParticipantService;

	@EventListener
	public void updateOffset(EventAfterNewChatReceived event) {
		Optional<ChatRoomParticipant> byId = chatRoomParticipantService.findById(event.getParticipantId());

		chatRoomParticipantService.updateOffset(byId.orElseThrow(), event.getOffset());
	}
}
