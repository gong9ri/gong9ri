package com.ll.gong9ri.boundedContext.groupBuyChatRoom.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.event.EventAfterGroupBuyCreated;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.service.GroupBuyChatRoomService;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class CreatedChatRoomEventHandler {
	private final GroupBuyChatRoomService groupBuyChatRoomService;

	@EventListener
	public void createChatRoom(EventAfterGroupBuyCreated event) {
		groupBuyChatRoomService.createChatRoom(event.getGroupBuy());
	}
}
