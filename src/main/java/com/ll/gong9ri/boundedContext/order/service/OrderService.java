package com.ll.gong9ri.boundedContext.order.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;
import com.ll.gong9ri.boundedContext.order.entity.ProductOptionQuantity;
import com.ll.gong9ri.boundedContext.order.repository.OrderInfoRepository;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
	private final OrderInfoRepository orderInfoRepository;

	@Transactional(readOnly = true)
	public Optional<OrderInfo> findById(final Long id) {
		return orderInfoRepository.findById(id);
	}

	public RsData<OrderInfo> createOrder(final Member member, final Product product) {
		OrderInfo orderInfo = OrderInfo.of(member, product);
		orderInfoRepository.save(orderInfo);

		return RsData.of("S-1", "주문이 생성되었습니다.", orderInfo);
	}

	public RsData<OrderInfo> confirmOrder(
		final Long memberId,
		final Long orderId,
		final Map<ProductOption, Integer> rawQuantities
	) {
		Optional<OrderInfo> oOrderInfo = orderInfoRepository.findById(orderId);
		if (oOrderInfo.isEmpty() || !oOrderInfo.get().getMemberId().equals(memberId)) {
			return RsData.failOf(null);
		}

		OrderInfo orderInfo = oOrderInfo.get();
		rawQuantities
			.entrySet()
			.stream()
			.map(e -> ProductOptionQuantity.builder()
				.orderInfo(orderInfo)
				.productOption(e.getKey())
				.quantity(e.getValue())
				.price(e.getKey().getProduct().getPrice() * e.getValue())
				.build())
				.forEach(orderInfo::addProductOptionQuantity);

		orderInfoRepository.save(orderInfo);

		return RsData.of("S-1", "옵션 선택이 완료되었습니다.", orderInfo);
	}
}