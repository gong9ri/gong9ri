package com.ll.gong9ri.boundedContext.memberAttr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.memberAttr.entity.MemberAttr;

public interface MemberAttrRepository extends JpaRepository<MemberAttr, Long> {
	Optional<MemberAttr> findByMemberId(final Long memberId);
}
