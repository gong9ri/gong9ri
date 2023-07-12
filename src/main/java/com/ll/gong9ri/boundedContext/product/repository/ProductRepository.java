package com.ll.gong9ri.boundedContext.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Boolean existsByIdAndStoreId(final Long id, final Long storeId);

	List<Product> findDistinctByNameLike(final String keyword);

	List<Product> findByStoreId(final Long storeId);

	Integer countByStoreId(final Long storeId);
}
