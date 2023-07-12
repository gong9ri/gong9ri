package com.ll.gong9ri.boundedContext.order.service;

import java.util.List;
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

	public RsData<OrderLog> create(final OrderInfo orderInfo, final List<ProductOptionQuantity> quantities) {
		OrderLog orderLog = OrderLog.builder()
			.orderId(orderInfo.getEncodedOrderId())
			.name(orderInfo.getName())
			.memberId(orderInfo.getMember().getId())
			.username(orderInfo.getMember().getUsername())
			.storeId(orderInfo.getStore().getId())
			.storeName(orderInfo.getStore().getName())
			.productId(orderInfo.getProduct().getId())
			.productName(orderInfo.getProduct().getName())
			.price(orderInfo.getProduct().getPrice())
			.salePrice(orderInfo.getPrice())
			.orderStatus(OrderStatus.CREATED)
			.totalPrice(orderInfo.getPrice() * quantities.stream()
				.mapToInt(ProductOptionQuantity::getQuantity)
				.sum())
			.productOptionQuantities(quantities)
			.build();

		return RsData.of("S-1", "주문이 생성되었습니다.", orderLogRepository.save(orderLog));
	}

	public RsData<OrderLog> groupBuyConfirm(
		final OrderLog groupBuyOrderLog,
		final List<ProductOptionQuantity> quantities
	) {
		if (!groupBuyOrderLog.getOrderStatus().equals(OrderStatus.GROUP_BUY_CREATED)) {
			return RsData.of("F-1", OrderStatus.GROUP_BUY_CREATED + " 상태의 주문이 아닙니다.", null);
		}

		OrderLog orderLog = groupBuyOrderLog.newLogOf().toBuilder()
			.orderStatus(OrderStatus.CREATED)
			.productOptionQuantities(quantities)
			.totalPrice(quantities.stream()
				.mapToInt(ProductOptionQuantity::getQuantity)
				.sum() * groupBuyOrderLog.getSalePrice())
			.build();

		return RsData.of("S-1", "옵션 선택이 완료되었습니다.", orderLogRepository.save(orderLog));
	}

	public RsData<OrderLog> confirm(
		final OrderLog createOrderLog,
		final OrderRecipientDTO orderRecipientDTO
	) {
		if (!createOrderLog.getOrderStatus().equals(OrderStatus.CREATED)) {
			return RsData.of("F-1", OrderStatus.CREATED + " 상태의 주문이 아닙니다.", null);
		}

		OrderLog orderLog = createOrderLog.newLogOf().toBuilder()
			.orderStatus(OrderStatus.RECIPIENT_DONE)
			.recipient(orderRecipientDTO.getRecipient())
			.phoneNumber(orderRecipientDTO.getPhoneNumber())
			.mainAddress(orderRecipientDTO.getMainAddress())
			.subAddress(orderRecipientDTO.getSubAddress())
			.build();

		return RsData.of("S-1", "옵션 선택이 완료되었습니다.", orderLogRepository.save(orderLog));
	}

	public RsData<OrderLog> payment(final OrderLog confirmedOrderLog, final String paymentKey) {
		if (!confirmedOrderLog.getOrderStatus().equals(OrderStatus.RECIPIENT_DONE)) {
			return RsData.of("F-1", OrderStatus.RECIPIENT_DONE + " 상태의 주문이 아닙니다.", null);
		}

		OrderLog orderLog = confirmedOrderLog.newLogOf().toBuilder()
				.orderStatus(OrderStatus.PURCHASE_REQUESTED)
				.paymentKey(paymentKey)
				.build();

		return RsData.of("S-1", "결제를 생성했습니다.", orderLogRepository.save(orderLog));
	}
}