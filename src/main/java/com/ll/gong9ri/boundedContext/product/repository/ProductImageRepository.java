package com.ll.gong9ri.boundedContext.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.product.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
	Optional<ProductImage> findByProductId(final Long productId);
}
