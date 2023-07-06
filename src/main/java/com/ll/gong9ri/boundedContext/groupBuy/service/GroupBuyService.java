package com.ll.gong9ri.boundedContext.groupBuy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyDTO;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyMember;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;
import com.ll.gong9ri.boundedContext.groupBuy.repository.GroupBuyMemberRepository;
import com.ll.gong9ri.boundedContext.groupBuy.repository.GroupBuyRepository;
import com.ll.gong9ri.boundedContext.groupBuy.repository.GroupBuyRepositoryImpl;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.product.entity.Product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupBuyService {
	private final GroupBuyRepository groupBuyRepository;
	private final GroupBuyRepositoryImpl groupBuyRepositoryImpl;
	private final GroupBuyMemberService groupBuyMemberService;

	@Transactional
	public RsData<GroupBuy> createGroupBuy(Product product) {

		GroupBuy groupBuy = GroupBuy.builder()
			.product(product)
			.name(product.getName())
			.startDate(LocalDateTime.now())
			.endDate(LocalDateTime.now().plusDays(1)) // 시작시간으로부터 하루 뒤
			.status(GroupBuyStatus.PROGRESS)
			.build();

		groupBuyRepository.save(groupBuy);

		return RsData.successOf(groupBuy);
	}

	public RsData canParticipateGroupBuy(GroupBuy groupBuy, Member member) {
		if(groupBuy == null){
			return RsData.of("F-1", "존재하지 않는 공동구매 입니다.");
		}
		if (groupBuyMemberService.isExistGroupByMember(groupBuy, member)) {
			return RsData.of("F-2", "이미 공동구매에 참여중인 사용자입니다.");
		}
		if (groupBuy.getStatus() != GroupBuyStatus.PROGRESS) {
			return RsData.of("F-3", "진행중인 공동구매가 아닙니다.");
		}
		return RsData.of("S-1", "공동구매 참여 성공");
	}

	@Transactional
	public RsData participateGroupBuy(GroupBuy groupBuy, Member member){
		RsData canParticipateGroupBuy = canParticipateGroupBuy(groupBuy, member);

		if(canParticipateGroupBuy.isFail()){
			return canParticipateGroupBuy;
		}

		return groupBuyMemberService.addGroupBuyMember(member, groupBuy);
	}

	public Optional<GroupBuy> findById(Long id) {
		return groupBuyRepository.findById(id);
	}

	public List<GroupBuy> findAll() {
		return groupBuyRepository.findAll();
	}

	public List<GroupBuyDTO> findAllByDTO() {
		return groupBuyRepositoryImpl.findAllGroupBuyMemberCount();

	}

	@Transactional
	public RsData<GroupBuy> save(GroupBuy groupBuy) {
		return RsData.successOf(groupBuyRepository.save(groupBuy));
	}
}
