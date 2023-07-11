package com.ll.gong9ri.boundedContext.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.product.entity.ProductOption;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
	Optional<ProductOption> findByIdAndDeleteStatusFalse(final Long id);

	List<ProductOption> findAllByProductIdAndDeleteStatusFalse(final Long productId);

	Optional<ProductOption> findByOptionOneNameAndOptionTwoName(String optionOneName, String optionTwoName);
}
