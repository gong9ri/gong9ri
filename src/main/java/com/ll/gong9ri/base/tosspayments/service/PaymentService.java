package com.ll.gong9ri.base.tosspayments.service;

import com.ll.gong9ri.base.tosspayments.entity.PaymentConfirmBody;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.base.tosspayments.entity.PaymentCreateBody;
import com.ll.gong9ri.base.tosspayments.entity.PaymentWebClient;
import com.ll.gong9ri.boundedContext.order.entity.OrderLog;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {
	private final PaymentWebClient paymentWebClient;

	public RsData<Boolean> createPayment(final OrderLog orderLog) {
		final PaymentCreateBody paymentCreateBody = PaymentCreateBody.builder()
			.method("카드")
			.amount(orderLog.getTotalPrice())
			.orderId(orderLog.getOrderId())
			.orderName(orderLog.getName())
			.build();

		Map paymentResult = paymentWebClient.paymentCreate(paymentCreateBody);
		if (!paymentResult.get("status").equals("DONE")) {
			return RsData.of("F-1", "결제 실패했습니다.", false);
		}

		return RsData.of("S-1", "결제 성공했습니다.", true);
	}

	public RsData<Boolean> confirmPayment(final OrderLog orderLog) {
		final PaymentConfirmBody paymentConfirmBody = PaymentConfirmBody.builder()
				.paymentKey(orderLog.getPaymentKey())
				.amount(orderLog.getTotalPrice())
				.orderId(orderLog.getOrderId())
				.build();

		Map paymentResult = paymentWebClient.paymentConfirm(paymentConfirmBody);
		if (!paymentResult.get("status").equals("DONE")) {
			return RsData.of("F-1", "결제 실패했습니다.", false);
		}

		return RsData.of("S-1", "결제 성공했습니다.", true);
	}
}
