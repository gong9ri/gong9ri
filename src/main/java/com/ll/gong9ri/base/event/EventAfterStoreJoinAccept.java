package com.ll.gong9ri.base.event;

import com.ll.gong9ri.boundedContext.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventAfterStoreJoinAccept {
	private Member member;
	private String storeName;
}
