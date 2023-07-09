package com.ll.gong9ri.boundedContext.groupBuy.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupBuyMemberRole {
	LEADER("총대"),
	STORE("판매자"),
	GENERAL("참여자");

	private final String value;
}
