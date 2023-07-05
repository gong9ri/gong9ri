package com.ll.gong9ri.boundedContext.groupBuy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyMember;

import java.util.List;
import java.util.Optional;

public interface GroupBuyMemberRepository extends JpaRepository<GroupBuyMember, Long> {
	List<GroupBuyMember> findAllByMemberId(final Long memberId);
	List<GroupBuyMember> findAllByGroupBuyId(final Long groupBuyId);

	Optional<GroupBuyMember> findByGroupBuyIdAndMemberId(Long groupBuyId, Long memberId);

	Integer countByGroupBuyId(final Long groupId);

}
