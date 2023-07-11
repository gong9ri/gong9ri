package com.ll.gong9ri.boundedContext.order.service;

import java.util.List;
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

	/**
	 * Member 가 직접 Product 를 구매합니다.
	 *
	 * @param member 구매한 Member
	 * @param product 구매할 Product
	 * @param quantities 구매할 Product 의 Option 과 개수 List
	 * @return OrderInfo CREATED
	 */
	public RsData<OrderInfo> create(
		final Member member,
		final Product product,
		final List<ProductOptionQuantity> quantities
	) {
		final Integer totCount = quantities.stream().mapToInt(ProductOptionQuantity::getQuantity).sum();

		OrderInfo orderInfo = OrderInfo.builder()
			.name(product.getName() + " " + totCount + "개")
			.member(member)
			.store(product.getStore())
			.product(product)
			.orderStatus(OrderStatus.CREATED)
			.price(product.getPrice())
			.build();

		orderInfo = repository.save(orderInfo);

		final RsData<OrderLog> rsOrderLog = orderLogService.create(orderInfo, quantities);
		if (rsOrderLog.isFail()) {
			return RsData.of(rsOrderLog.getResultCode(), rsOrderLog.getMsg(), null);
		}

		orderInfo = orderInfo.toBuilder()
			.recentOrderLogId(rsOrderLog.getData().getId())
			.build();

		return RsData.of("S-1", "주문이 생성되었습니다.", repository.save(orderInfo));
	}

	/**
	 * GroupBuy 에 의해 생성된 Order 에 옵션을 선택해 구매를 결정합니다.
	 *
	 * @param groupBuyOrderInfo GroupBuy 가 생성한 Order
	 * @param quantities 구매할 Product 의 Option 과 개수 List
	 * @return OrderInfo CREATED
	 */
	public RsData<OrderInfo> groupBuyConfirm(
		final OrderInfo groupBuyOrderInfo,
		final List<ProductOptionQuantity> quantities
	) {
		final Optional<OrderLog> createdOrderLog = orderLogService.findById(groupBuyOrderInfo.getRecentOrderLogId());
		if (createdOrderLog.isEmpty()) {
			return RsData.failOf(null);
		}

		final RsData<OrderLog> rsOrderLog = orderLogService.groupBuyConfirm(
			createdOrderLog.get(),
			quantities
		);

		final OrderInfo orderInfo = groupBuyOrderInfo.toBuilder()
			.recentOrderLogId(rsOrderLog.getData().getId())
			.orderStatus(OrderStatus.CREATED)
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

	public RsData<OrderInfo> confirm(
		final OrderInfo createdOrderInfo,
		final OrderRecipientDTO orderRecipientDTO
	) {
		final Optional<OrderLog> createdOrderLog = orderLogService.findById(createdOrderInfo.getRecentOrderLogId());
		if (createdOrderLog.isEmpty()) {
			return RsData.failOf(null);
		}

		final RsData<OrderLog> rsOrderLog = orderLogService.confirm(createdOrderLog.get(), orderRecipientDTO);
		if (rsOrderLog.isFail()) {
			return RsData.of(rsOrderLog.getResultCode(), rsOrderLog.getMsg(), null);
		}

		final OrderInfo orderInfo = createdOrderInfo.toBuilder()
			.recentOrderLogId(rsOrderLog.getData().getId())
			.orderStatus(OrderStatus.RECIPIENT_DONE)
			.build();

		repository.save(orderInfo);

		return RsData.of("S-1", "옵션 선택이 완료되었습니다.", orderInfo);
	}
}