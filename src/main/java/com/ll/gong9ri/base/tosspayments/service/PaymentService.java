package com.ll.gong9ri.base.tosspayments.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.base.tosspayments.dto.PaymentResultDTO;
import com.ll.gong9ri.base.tosspayments.entity.PaymentCreateBody;
import com.ll.gong9ri.base.tosspayments.entity.PaymentResult;
import com.ll.gong9ri.base.tosspayments.entity.PaymentWebClient;
import com.ll.gong9ri.boundedContext.order.entity.OrderLog;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {
	private final PaymentWebClient paymentWebClient;

	public RsData<PaymentResult> createPayment(final OrderLog orderLog) {
		final PaymentCreateBody paymentCreateBody = PaymentCreateBody.builder()
			.method("카드")
			.amount(orderLog.getTotalPrice())
			.orderId(orderLog.getOrderId())
			.orderName(orderLog.getName())
			.build();

		PaymentResultDTO paymentResultDto = paymentWebClient.paymentCreate(paymentCreateBody);
		// TODO: paymentResultRepository.save(paymentResult)
		//paymentResultRepository.save(paymentResult);
		return RsData.successOf(paymentResultDto.toEntity());
	}
}
