package com.ll.gong9ri.boundedContext.groupBuy.entity;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupBuyStatus {
	WAIT("대기"),
	PROGRESS("모집 진행"),
	ORDER("주문"), // 모집 완료 후 구매자가 주문 진행중인 상태
	EXPIRE("만료"),
	DONE("완료");

	private final String value;
	public static GroupBuyStatus of(String status){
		return Arrays.stream(GroupBuyStatus.values())
			.filter(e -> e.getValue().equals(status))
			.findFirst()
			.orElse(GroupBuyStatus.WAIT);
	}
}
