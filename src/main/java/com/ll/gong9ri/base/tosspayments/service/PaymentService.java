package com.ll.gong9ri.base.tosspayments.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.base.tosspayments.dto.PaymentResultDTO;
import com.ll.gong9ri.base.tosspayments.entity.PaymentCreateBody;
import com.ll.gong9ri.base.tosspayments.entity.PaymentResult;
import com.ll.gong9ri.base.tosspayments.entity.PaymentWebClient;
import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;
import com.ll.gong9ri.boundedContext.order.entity.ProductOptionQuantity;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {
	// public final PaymentResultRepository paymentResultRepository;

	public RsData<PaymentResult> createPayment(final OrderInfo orderInfo) {
		PaymentCreateBody paymentCreateBody = PaymentCreateBody.builder()
			.method("카드")
			.amount(orderInfo.getProductOptionQuantities()
				.stream()
				.mapToInt(ProductOptionQuantity::getPrice)
				.sum())
			//.orderId(orderInfo.getId().toString()) // TODO: Base 64 encode
			.orderId(Base64.getEncoder()
				.encodeToString(String.valueOf("oId"+orderInfo.getId()).getBytes(StandardCharsets.UTF_8))) // Base 64 encode
			.orderName(orderInfo.getProductName()
				+ " "
				+ orderInfo.getProductOptionQuantities()
				.stream()
				.mapToInt(ProductOptionQuantity::getQuantity)
				.sum()
				+ "개"
			)
			.build();

		System.out.println(paymentCreateBody);

		PaymentResultDTO paymentResultDto = PaymentWebClient.paymentCreate(paymentCreateBody);
		// TODO: paymentResultRepository.save(paymentResult)
		//paymentResultRepository.save(paymentResult);
		return RsData.successOf(paymentResultDto.toEntity());
	}
}
