package com.ll.gong9ri.boundedContext.groupBuy.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;

public interface GroupBuyRepository extends JpaRepository<GroupBuy, Long> {
	Boolean existsByProductIdAndStatus(final Long productId, final GroupBuyStatus status);

	List<GroupBuy> findByStatusAndEndDateBefore(GroupBuyStatus status, LocalDateTime endDate);
/*
	@Query("SELECT g FROM GroupBuy g WHERE g.name LIKE %:keyword%")

	Page<GroupBuy>findByKeyword(@Param("keyword") String keyword, Pageable pageable);*/
}
