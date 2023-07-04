package com.ll.gong9ri.boundedContext.order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;

public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {
	Optional<OrderInfo> findById(final Long id);
}
