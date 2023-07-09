package com.ll.gong9ri.boundedContext.order.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.order.dto.OrderRecipientDTO;
import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;
import com.ll.gong9ri.boundedContext.order.entity.OrderLog;
import com.ll.gong9ri.boundedContext.order.entity.OrderStatus;
import com.ll.gong9ri.boundedContext.order.entity.ProductOptionQuantity;
import com.ll.gong9ri.boundedContext.order.repository.OrderInfoRepository;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderInfoService {
	private final OrderInfoRepository repository;
	private final OrderLogService orderLogService;

	@Transactional(readOnly = true)
	public Optional<OrderInfo> findById(final Long id) {
		return repository.findById(id);
	}

	@Transactional(readOnly = true)
	public List<OrderInfo> findByMemberId(final Long memberId) {
		return repository.findAllByMemberId(memberId);
	}

	@Transactional(readOnly = true)
	public List<OrderInfo> findByStoreId(final Long storeId) {
		return repository.findAllByStoreId(storeId);
	}

	/**
	 * OrderInfo 를 생성하고 OrderId 를 받습니다.
	 * @param member
	 * @param product
	 * @return OrderInfo
	 */
	public RsData<OrderInfo> preCreate(final Member member, final Product product) {
		OrderInfo orderInfo = OrderInfo.builder()
			.member(member)
			.store(product.getStore())
			.product(product)
			.price(product.getPrice())
			.orderStatus(OrderStatus.PRE_CREATED)
			.build();

		repository.save(orderInfo);

		return RsData.successOf(orderInfo);
	}

	/**
	 * GroupBuy 가 OrderInfo 를 생성하고 OrderId 를 받습니다.
	 * @param member
	 * @param product
	 * @param salePrice
	 * @return OrderInfo
	 */
	public RsData<OrderInfo> groupBuyCreate(final Member member, final Product product, final Integer salePrice) {
		OrderInfo orderInfo = OrderInfo.builder()
			.member(member)
			.store(product.getStore())
			.product(product)
			.price(salePrice)
			.orderStatus(OrderStatus.GROUP_BUY_CREATED)
			.build();

		repository.save(orderInfo);

		return RsData.successOf(orderInfo);
	}

	public RsData<OrderInfo> create(final OrderInfo preCreatedOrderInfo) {
		final RsData<OrderLog> rsOrderLog = orderLogService.create(preCreatedOrderInfo);
		if (rsOrderLog.isFail()) {
			return RsData.of(rsOrderLog.getResultCode(), rsOrderLog.getMsg(), null);
		}

		OrderInfo orderInfo = preCreatedOrderInfo.toBuilder()
			.orderStatus(OrderStatus.CREATED)
			.recentOrderLogId(rsOrderLog.getData().getId())
			.build();

		repository.save(orderInfo);

		return RsData.of("S-1", "주문이 생성되었습니다.", orderInfo);
	}

	public RsData<OrderInfo> confirm(
		final OrderInfo createdOrderInfo,
		final OrderRecipientDTO orderRecipientDTO,
		final Map<ProductOption, Integer> options
	) {
		final Optional<OrderLog> createdOrderLog = orderLogService.findById(createdOrderInfo.getRecentOrderLogId());
		if (createdOrderLog.isEmpty()) {
			return RsData.failOf(null);
		}

		final RsData<OrderLog> rsOrderLog = orderLogService.confirm(createdOrderLog.get(), orderRecipientDTO, options);

		final OrderInfo orderInfo = createdOrderInfo.toBuilder()
			.recentOrderLogId(rsOrderLog.getData().getId())
			.orderStatus(OrderStatus.OPTION_SELECTED)
			.name(rsOrderLog.getData().getProductName()
				+ " "
				+ rsOrderLog.getData().getProductOptionQuantities()
				.stream()
				.mapToInt(ProductOptionQuantity::getQuantity)
				.sum()
				+ "개")
			.build();

		repository.save(orderInfo);

		return RsData.of("S-1", "옵션 선택이 완료되었습니다.", orderInfo);
	}
}