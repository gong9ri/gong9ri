package com.ll.gong9ri.boundedContext.memberAttr.eventListener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.event.EventAfterMemberJoinAttr;
import com.ll.gong9ri.boundedContext.memberAttr.service.MemberAttrService;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class MemberAttrEventListener {
	private final MemberAttrService memberAttrService;

	@EventListener
	public void listen(EventAfterMemberJoinAttr event) {
		memberAttrService.create(event.getMember());
	}
}
