package com.ll.gong9ri.boundedContext.order.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;
import com.ll.gong9ri.boundedContext.order.entity.ProductOptionQuantity;
import com.ll.gong9ri.boundedContext.order.repository.OrderRepository;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;
import com.ll.gong9ri.boundedContext.product.service.ProductService;
import com.ll.gong9ri.boundedContext.store.entity.Store;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final ProductService productService;

	public RsData<OrderInfo> createOrder(final Member member, final Map<Long, Integer> rawQuantities) {
		List<ProductOptionQuantity> quantities = rawQuantities
			.values()
			.stream()
			//ProductOption productOption = productService.getProductOptionById(el.getKey());
			.map(integer -> ProductOptionQuantity.builder()
				.productOption(ProductOption.builder().product(null).build())
				.quantity(integer)
				.build())
			.collect(Collectors.toList());

		Product product = quantities.get(0).getProductOption().getProduct();
		Store store = product.getStore();
		OrderInfo orderInfo = OrderInfo.of(member, store, product, quantities);

		orderRepository.save(orderInfo);

		return RsData.of("S-1", "주문이 생성되었습니다.", orderInfo);
	}
}

