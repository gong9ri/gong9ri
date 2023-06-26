package com.ll.gong9ri.boundedContext.store.eventListener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.event.EventAfterStoreJoinAccept;
import com.ll.gong9ri.boundedContext.store.service.StoreService;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class StoreEventListener {
	private final StoreService storeService;

	@EventListener
	public void listen(EventAfterStoreJoinAccept event) {
		storeService.create(event.getMember(), event.getStoreName());
	}
}
