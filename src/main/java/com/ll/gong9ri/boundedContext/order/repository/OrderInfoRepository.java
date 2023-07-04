package com.ll.gong9ri.boundedContext.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;

public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {
	List<OrderInfo> findAllByMemberId(final Long memberId);

	List<OrderInfo> findAllByProductId(final Long productId);

	List<OrderInfo> findAllByStoreId(final Long storeId);
}
