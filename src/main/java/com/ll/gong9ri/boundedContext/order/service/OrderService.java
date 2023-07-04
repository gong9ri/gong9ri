package com.ll.gong9ri.boundedContext.order.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;
import com.ll.gong9ri.boundedContext.order.repository.OrderInfoRepository;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
	private final OrderInfoRepository orderInfoRepository;
	private final ProductOptionQuantityService productOptionQuantityService;

	@Transactional(readOnly = true)
	public Optional<OrderInfo> findById(final Long id) {
		return orderInfoRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public List<OrderInfo> findByMemberId(final Long memberId) {
		return orderInfoRepository.findAllByMemberId(memberId);
	}

	@Transactional(readOnly = true)
	public List<OrderInfo> findByStoreId(final Long storeId) {
		return orderInfoRepository.findAllByStoreId(storeId);
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
		final Optional<OrderInfo> oOrderInfo = orderInfoRepository.findById(orderId);
		if (oOrderInfo.isEmpty() || !oOrderInfo.get().getMemberId().equals(memberId)) {
			return RsData.failOf(null);
		}

		final OrderInfo orderInfo = oOrderInfo.get().toBuilder()
			.productOptionQuantities(rawQuantities.entrySet()
				.stream()
				.map(el -> productOptionQuantityService.create(oOrderInfo.get(), el.getKey(), el.getValue()))
				.toList())
			.build();

		orderInfoRepository.save(orderInfo);

		return RsData.of("S-1", "옵션 선택이 완료되었습니다.", orderInfo);
	}
}