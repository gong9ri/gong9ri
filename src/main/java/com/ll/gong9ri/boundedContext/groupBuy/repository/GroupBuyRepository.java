package com.ll.gong9ri.boundedContext.groupBuy.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;

public interface GroupBuyRepository extends JpaRepository<GroupBuy, Long> {
	Boolean existsByProductIdAndStatus(final Long productId, final GroupBuyStatus status);

	@Query("SELECT gb FROM GroupBuy gb WHERE gb.status = :status AND gb.endDate < :endDate")
	List<GroupBuy> findByStatusAndEndDateBefore(@Param("status") GroupBuyStatus status, @Param("endDate") LocalDateTime endDate);
}
