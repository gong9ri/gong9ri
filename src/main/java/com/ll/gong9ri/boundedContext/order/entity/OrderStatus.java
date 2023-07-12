package com.ll.gong9ri.boundedContext.order.entity;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
	PRE_CREATE("주문생성준비"),
	GROUP_BUY_CREATED("공동구매주문생성됨"),
	CREATED("주문생성됨"),
	RECIPIENT_DONE("배송정보입력됨"),
	PURCHASE_REQUESTED("결제요청됨"),
	PURCHASE_SUCCESS("결제성공"),
	PURCHASE_CANCEL("결제취소"),
	PURCHASE_FAIL("결제실패"),
	SHIPPING_PROGRESS("배송중"),
	SHIPPING_CANCEL("배송취소"),
	SHIPPING_FAIL("배송실패"),
	SHIPPING_DONE("배송완료"),
	ORDER_DONE("주문완료");

	private final String status;

	public static OrderStatus statusOf(final String rawStatus) {
		return Arrays.stream(values()).filter(e -> e.getStatus().equals(rawStatus))
			.findFirst()
			.orElse(PRE_CREATE);
	}
}
