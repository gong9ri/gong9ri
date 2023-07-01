package com.ll.gong9ri.boundedContext.storeChat.eventListener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.event.EventAfterStoreChatMessageCreate;
import com.ll.gong9ri.boundedContext.storeChat.service.StoreChatRoomService;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class StoreChatRoomEventListener {
	private final StoreChatRoomService roomService;

	@EventListener
	public void listen(EventAfterStoreChatMessageCreate event) {
		roomService.setOffset(event.getRoom(), event.getOffset(), event.getIsMember());
	}
}
