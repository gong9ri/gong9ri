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
	private static final String ORDER_ID_PREFIX = "oId";
	private final PaymentWebClient paymentWebClient;

	/**
	 * @param rawOrderId
	 * @return add ORDER_ID_PREFIX, base64 encoded string
	 */
	public String getEncodedOrderId(final Long rawOrderId) {
		return Base64.getEncoder().encodeToString((ORDER_ID_PREFIX + rawOrderId).getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * @param encodedOrderId
	 * @return remove ORDER_ID_PREFIX, decoded Long id
	 */
	public Long getDecodedOrderId(final String encodedOrderId) {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedOrderId);
		String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

		return Long.parseLong(decodedString.substring(ORDER_ID_PREFIX.length()));
	}

	public RsData<PaymentResult> createPayment(final OrderInfo orderInfo) {
		PaymentCreateBody paymentCreateBody = PaymentCreateBody.builder()
			.method("카드")
			.amount(orderInfo.getProductOptionQuantities()
				.stream()
				.mapToInt(ProductOptionQuantity::getPrice)
				.sum()
			)
			.orderId(getEncodedOrderId(orderInfo.getId())) // Base 64 encode
			.orderName(orderInfo.getProductName()
				+ " "
				+ orderInfo.getProductOptionQuantities()
				.stream()
				.mapToInt(ProductOptionQuantity::getQuantity)
				.sum()
				+ "개"
			)
			.build();

		PaymentResultDTO paymentResultDto = paymentWebClient.paymentCreate(paymentCreateBody);
		// TODO: paymentResultRepository.save(paymentResult)
		//paymentResultRepository.save(paymentResult);
		return RsData.successOf(paymentResultDto.toEntity());
	}
}
