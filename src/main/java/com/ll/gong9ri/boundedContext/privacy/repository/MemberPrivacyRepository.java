package com.ll.gong9ri.boundedContext.privacy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.privacy.entity.MemberPrivacy;

public interface MemberPrivacyRepository extends JpaRepository<MemberPrivacy, Long> {
	Optional<MemberPrivacy> findByMemberId(final Long memberId);
}