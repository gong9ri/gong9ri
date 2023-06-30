package com.ll.gong9ri.boundedContext.product.repository;

import com.ll.gong9ri.boundedContext.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findDistinctByNameLike(final String keyword);
}
