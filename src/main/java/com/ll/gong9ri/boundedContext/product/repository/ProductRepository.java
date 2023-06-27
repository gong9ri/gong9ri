package com.ll.gong9ri.boundedContext.product.repository;

import com.ll.gong9ri.boundedContext.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
