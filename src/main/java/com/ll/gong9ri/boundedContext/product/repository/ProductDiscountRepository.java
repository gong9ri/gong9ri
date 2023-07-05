package com.ll.gong9ri.boundedContext.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.product.entity.ProductDiscount;

public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Long> {
	Optional<ProductDiscount> findByIdAndDeleteStatusFalse(final Long id);

	List<ProductDiscount> findAllByProductIdAndDeleteStatusFalse(final Long productId);
}
