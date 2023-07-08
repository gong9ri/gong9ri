package com.ll.gong9ri.boundedContext.groupBuy.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;

public interface GroupBuyRepository extends JpaRepository<GroupBuy, Long> {
	Boolean existsByProductIdAndStatus(final Long productId, final GroupBuyStatus status);

	List<GroupBuy> findByStatusAndEndDateBefore(GroupBuyStatus status, LocalDateTime endDate);
}
