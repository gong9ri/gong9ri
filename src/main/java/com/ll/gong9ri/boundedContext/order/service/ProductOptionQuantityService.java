package com.ll.gong9ri.boundedContext.order.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;
import com.ll.gong9ri.boundedContext.order.entity.ProductOptionQuantity;
import com.ll.gong9ri.boundedContext.order.repository.ProductOptionQuantityRepository;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductOptionQuantityService {
	private final ProductOptionQuantityRepository repository;

	@Transactional(readOnly = true)
	public Optional<ProductOptionQuantity> findById(final Long id) {
		return repository.findById(id);
	}

	@Transactional(readOnly = true)
	public List<ProductOptionQuantity> findByOrderId(final Long orderInfoId) {
		return repository.findAllByOrderInfoId(orderInfoId);
	}

	@Transactional(readOnly = true)
	public List<ProductOptionQuantity> findByProductId(final Long productId) {
		return repository.findAllByProductOption_ProductId(productId);
	}

	public ProductOptionQuantity create(
		final OrderInfo orderInfo,
		final ProductOption option,
		final Integer quantity
	) {
		ProductOptionQuantity productOptionQuantity = ProductOptionQuantity.builder()
			.orderInfo(orderInfo)
			.productOption(option)
			.quantity(quantity)
			.price(option.getProduct().getPrice() * quantity)
			.build();

		repository.save(productOptionQuantity);

		return productOptionQuantity;
	}
}
