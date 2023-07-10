package com.ll.gong9ri.boundedContext.order.eventListener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.event.EventGroupBuyProgress;
import com.ll.gong9ri.boundedContext.order.service.OrderInfoService;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class OrderEventListener {
	private final OrderInfoService orderInfoService;

	@EventListener
	public void listen(EventGroupBuyProgress event) {
		orderInfoService.groupBuyCreate(event.getMember(), event.getProduct(), event.getSalePrice());
	}
}
