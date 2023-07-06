package com.ll.gong9ri.boundedContext.groupBuy.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyMember;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyMemberRole;
import com.ll.gong9ri.boundedContext.groupBuy.repository.GroupBuyMemberRepository;
import com.ll.gong9ri.boundedContext.groupBuy.repository.GroupBuyRepository;
import com.ll.gong9ri.boundedContext.member.entity.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupBuyMemberService {
	private final GroupBuyMemberRepository groupBuyMemberRepository;
	private final GroupBuyRepository groupBuyRepository;

	@Transactional
	public List<GroupBuyMember> addFirstGroupBuyMember(Member member, GroupBuy groupBuy){
		List<GroupBuyMember> groupBuyMembers = groupBuyMemberRepository.findAllByGroupBuyId(groupBuy.getId());

		GroupBuyMember groupBuyMember = GroupBuyMember.builder()
			.member(member)
			.role(GroupBuyMemberRole.LEADER)
			.groupBuy(groupBuy)
			.build();

		groupBuyMembers.add(groupBuyMember);
		groupBuyMemberRepository.saveAll(groupBuyMembers);

		return groupBuyMembers;
	}

	@Transactional
	public RsData<List<GroupBuyMember>> addGroupBuyMember(Member member, GroupBuy groupBuy){
		List<GroupBuyMember> groupBuyMembers = groupBuyMemberRepository.findAllByGroupBuyId(groupBuy.getId());

		GroupBuyMember groupBuyMember = GroupBuyMember.builder()
			.member(member)
			.role(GroupBuyMemberRole.GENERAL)
			.groupBuy(groupBuy)
			.build();

		groupBuyMembers.add(groupBuyMember);

		groupBuyMemberRepository.saveAll(groupBuyMembers);

		return RsData.successOf(groupBuyMembers);
	}

	public List<GroupBuyMember> findAllByGroupBuyId(Long groupBuyId){
		return groupBuyMemberRepository.findAllByGroupBuyId(groupBuyId);
	}

	public Integer getMemberCount(Long groupId) {
		return groupBuyMemberRepository.countByGroupBuyId(groupId);
	}

	public boolean isExistGroupByMember(GroupBuy groupBuy, Member member){
		return groupBuyMemberRepository
			.findByGroupBuyIdAndMemberId(groupBuy.getId(), member.getId())
			.isPresent();
	}
}
