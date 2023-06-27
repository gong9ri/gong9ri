package com.ll.gong9ri.boundedContext.store.repository;

import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByMemberId(final Long id);

    Optional<Store> findByName(final String name);

    List<Store> findDistinctByNameLike(final String input);

    Optional<Store> findByMember(final Member member);
}
