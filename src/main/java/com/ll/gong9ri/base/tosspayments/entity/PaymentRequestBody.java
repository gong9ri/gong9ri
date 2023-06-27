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
public class PaymentRequestBody {
	// TODO: https://docs.tosspayments.com/reference/test/v1/payments/confirm/POST
	private String method;
	private Integer amount;
	private String orderId;
	private String orderName;
	private String successUrl;
	private String failUrl;
}
