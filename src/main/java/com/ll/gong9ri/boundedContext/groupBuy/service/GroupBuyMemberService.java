package com.ll.gong9ri.boundedContext.groupBuy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyMember;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyMemberRole;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;
import com.ll.gong9ri.boundedContext.groupBuy.repository.GroupBuyMemberRepository;
import com.ll.gong9ri.boundedContext.member.entity.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GroupBuyMemberService {
	private final GroupBuyMemberRepository groupBuyMemberRepository;

	@Transactional(readOnly = true)
	public Optional<GroupBuyMember> findById(final Long id) {
		return groupBuyMemberRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public List<GroupBuyMember> findAllByGroupBuyId(final Long groupBuyId) {
		return groupBuyMemberRepository.findAllByGroupBuyId(groupBuyId);
	}

	@Transactional(readOnly = true)
	public Integer getMemberCount(final Long groupId) {
		return groupBuyMemberRepository.countByGroupBuyId(groupId);
	}

	@Transactional(readOnly = true)
	public boolean isExistGroupByMember(final GroupBuy groupBuy, final Member member) {
		return groupBuyMemberRepository.existsByGroupBuyIdAndMemberId(groupBuy.getId(), member.getId());
	}

	public RsData<Void> canParticipateGroupBuy(final GroupBuy groupBuy, final Member member) {
		if (groupBuy == null) {
			return RsData.of("F-1", "존재하지 않는 공동구매 입니다.");
		}

		if (groupBuy.getStatus() != GroupBuyStatus.PROGRESS) {
			return RsData.of("F-2", "진행중인 공동구매가 아닙니다.");
		}

		final Optional<GroupBuyMember> oGroupBuyMember = groupBuyMemberRepository.findByGroupBuyIdAndMemberId(
			groupBuy.getId(),
			member.getId()
		);

		if (oGroupBuyMember.isPresent()) {
			return RsData.of("F-3", "이미 참여중인 공동구매 입니다.");
		}

		return RsData.of("S-1", "공동구매 참여 성공");
	}

	public RsData<GroupBuyMember> create(final Member member, final GroupBuy groupBuy, final GroupBuyMemberRole role) {
		final RsData<Void> rsValidate = canParticipateGroupBuy(groupBuy, member);
		if (rsValidate.isFail()) {
			return RsData.of(rsValidate.getResultCode(), rsValidate.getMsg(), null);
		}

		GroupBuyMember groupBuyMember = GroupBuyMember.builder()
			.member(member)
			.role(role)
			.groupBuy(groupBuy)
			.build();

		groupBuyMemberRepository.save(groupBuyMember);

		return RsData.successOf(groupBuyMember);
	}

	public RsData<GroupBuyMember> addLeader(final Member member, final GroupBuy groupBuy) {
		return create(member, groupBuy, GroupBuyMemberRole.LEADER);
	}

	public RsData<GroupBuyMember> addStore(final Member member, final GroupBuy groupBuy) {
		return create(member, groupBuy, GroupBuyMemberRole.STORE);
	}

	public RsData<GroupBuyMember> addGeneral(final Member member, final GroupBuy groupBuy) {
		return create(member, groupBuy, GroupBuyMemberRole.GENERAL);
	}

	public RsData<GroupBuyMember> update(final GroupBuy groupBuy, final Member member, final GroupBuyMemberRole role) {
		final Optional<GroupBuyMember> oGroupBuyMember = groupBuyMemberRepository.findByGroupBuyIdAndMemberId(
			groupBuy.getId(),
			member.getId()
		);
		if (oGroupBuyMember.isEmpty() || oGroupBuyMember.get().getMember().getId().equals(member.getId())) {
			return RsData.of("F-1", "잘못된 접근입니다.", null);
		}

		GroupBuyMember groupBuyMember = oGroupBuyMember.get().toBuilder()
			.role(role)
			.build();

		groupBuyMemberRepository.save(groupBuyMember);

		return RsData.successOf(groupBuyMember);
	}

	@Transactional
	public RsData<Void> delete(final GroupBuy groupBuy, final Member member) {
		final Optional<GroupBuyMember> oGroupBuyMember = groupBuyMemberRepository.findByGroupBuyIdAndMemberId(
			groupBuy.getId(),
			member.getId()
		);
		if (oGroupBuyMember.isEmpty() || !oGroupBuyMember.get().getMember().getId().equals(member.getId())) {
			return RsData.of("F-1", "잘못된 접근입니다.");
		}

		if (!oGroupBuyMember.get().getRole().equals(GroupBuyMemberRole.GENERAL)) {
			return RsData.of("F-2", "참여자만 취소할 수 있습니다.");
		}

		groupBuyMemberRepository.delete(oGroupBuyMember.get());

		return RsData.of("S-1", groupBuy.getName() + " 참여를 취소했습니다.");
	}
}
