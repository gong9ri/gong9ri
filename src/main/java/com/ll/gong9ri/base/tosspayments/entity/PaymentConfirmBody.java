package com.ll.gong9ri.base.tosspayments.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfirmBody {
	private String paymentKey;
	private Integer amount;
	private String orderId;
}