package com.ll.gong9ri.boundedContext.order.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.order.dto.OrderRecipientDTO;
import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;
import com.ll.gong9ri.boundedContext.order.entity.OrderLog;
import com.ll.gong9ri.boundedContext.order.entity.OrderStatus;
import com.ll.gong9ri.boundedContext.order.entity.ProductOptionQuantity;
import com.ll.gong9ri.boundedContext.order.repository.OrderLogRepository;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderLogService {
	private final OrderLogRepository orderLogRepository;

	@Transactional(readOnly = true)
	public Optional<OrderLog> findById(final String id) {
		return orderLogRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public List<OrderLog> findAllByOrderId(final String orderId) {
		return orderLogRepository.findAllByOrderId(orderId);
	}

	public RsData<OrderLog> create(final OrderInfo orderInfo) {
		OrderLog orderLog = OrderLog.builder()
			.orderId(orderInfo.getEncodedOrderId())
			.memberId(orderInfo.getMember().getId())
			.username(orderInfo.getMember().getUsername())
			.storeId(orderInfo.getStore().getId())
			.storeName(orderInfo.getStore().getName())
			.productId(orderInfo.getProduct().getId())
			.productName(orderInfo.getProduct().getName())
			.price(orderInfo.getProduct().getPrice())
			.salePrice(orderInfo.getPrice())
			.orderStatus(OrderStatus.CREATED)
			.build();

		orderLogRepository.save(orderLog);

		return RsData.of("S-1", "주문이 생성되었습니다.", orderLog);
	}

	public RsData<OrderLog> confirm(
		final OrderLog createOrderLog,
		final OrderRecipientDTO orderRecipientDTO,
		final Map<ProductOption, Integer> rawQuantities
	) {
		if (!createOrderLog.getOrderStatus().equals(OrderStatus.CREATED)) {
			return RsData.of("F-1", OrderStatus.CREATED + " 상태의 주문이 아닙니다.", null);
		}

		final OrderLog orderLog = createOrderLog.newLogOf().toBuilder()
			.orderStatus(OrderStatus.OPTION_SELECTED)
			.recipient(orderRecipientDTO.getRecipient())
			.mainAddress(orderRecipientDTO.getMainAddress())
			.subAddress(orderRecipientDTO.getSubAddress())
			.productOptionQuantities(rawQuantities.entrySet()
				.stream()
				.map(el -> ProductOptionQuantity.builder()
					.optionDetail(el.getKey().getOptionDetail())
					.quantity(el.getValue())
					.build())
				.toList())
			.totalPrice(rawQuantities.values()
				.stream()
				.mapToInt(i -> i)
				.sum() * createOrderLog.getSalePrice())
			.build();

		orderLogRepository.save(orderLog);

		return RsData.of("S-1", "옵션 선택이 완료되었습니다.", orderLog);
	}

	public RsData<OrderLog> paymentRequest(final OrderLog optionSelectedOrderLog) {
		if (!optionSelectedOrderLog.getOrderStatus().equals(OrderStatus.OPTION_SELECTED)) {
			return RsData.of("F-1", OrderStatus.OPTION_SELECTED + " 상태의 주문이 아닙니다.", null);
		}
		// TODO: pr save

		return RsData.of("S-1", "결제 요청이 완료되었습니다.", null);
	}
}