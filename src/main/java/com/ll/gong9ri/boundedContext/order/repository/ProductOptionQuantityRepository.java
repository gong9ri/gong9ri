package com.ll.gong9ri.boundedContext.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.order.entity.ProductOptionQuantity;

public interface ProductOptionQuantityRepository extends JpaRepository<ProductOptionQuantity, Long> {
	List<ProductOptionQuantity> findAllByOrderInfoId(final Long orderId);

	List<ProductOptionQuantity> findAllByProductOption_ProductId(final Long productId);
}
