package com.ll.gong9ri.boundedContext.groupBuy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;

public interface GroupBuyRepository extends JpaRepository<GroupBuy, Long> {
	Optional<GroupBuy> findById(Long aLong);
}
