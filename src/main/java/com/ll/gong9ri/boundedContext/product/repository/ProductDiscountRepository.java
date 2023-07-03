package com.ll.gong9ri.boundedContext.product.repository;

import com.ll.gong9ri.boundedContext.product.entity.ProductDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Long> {

    Optional<ProductDiscount> findByHeadCountAndDiscountRate(Integer headCount, Integer discountRate);
}
