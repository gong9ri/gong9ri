package com.ll.gong9ri.boundedContext.groupBuy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyMember;

public interface GroupBuyMemberRepository extends JpaRepository<GroupBuyMember, Long> {
	List<GroupBuyMember> findAllByMemberId(final Long memberId);

	List<GroupBuyMember> findAllByGroupBuyId(final Long groupBuyId);

	Optional<GroupBuyMember> findByGroupBuyIdAndMemberId(final Long groupBuyId, final Long memberId);

	Integer countByGroupBuyId(final Long groupBuyId);

	Boolean existsByGroupBuyIdAndMemberId(final Long groupBuyId, final Long memberId);
}
