package com.ll.gong9ri.boundedContext.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
	Optional<Store> findByMemberId(final Long id);

	Optional<Store> findByName(final String name);

	List<Store> findDistinctByNameLike(final String input);
}
