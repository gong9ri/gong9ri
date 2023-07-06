package com.ll.gong9ri.boundedContext.groupBuy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;

public interface GroupBuyRepository extends JpaRepository<GroupBuy, Long> {
	Boolean existsByProductIdAndStatus(final Long productId, final String status);
}
