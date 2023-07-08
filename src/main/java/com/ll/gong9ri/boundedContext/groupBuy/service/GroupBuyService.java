package com.ll.gong9ri.boundedContext.groupBuy.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyDetailDTO;
import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyListDTO;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;
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

	public Optional<GroupBuy> getProgressGroupBuy(final Long id) {
		return groupBuyRepository.findById(id)
			.filter(e -> e.getStatus().equals(GroupBuyStatus.PROGRESS));
	}

	public Optional<GroupBuy> findById(final Long id) {
		return groupBuyRepository.findById(id);
	}

	public List<GroupBuy> findAll() {
		return groupBuyRepository.findAll();
	}

	public List<GroupBuyListDTO> getAllGroupBuyListDTO() {
		return groupBuyRepositoryImpl.getAllGroupBuyListDTO();
	}

	public List<GroupBuyListDTO> getAllGroupBuyListDTOInProgress() {
		return groupBuyRepositoryImpl.getAllGroupBuyListDTOInProgress();
	}

	public GroupBuyDetailDTO getGroupBuyDetailDTO(final Member member) {
		return groupBuyRepositoryImpl.getGroupBuyDetailDTO(member);
	}

	/**
	 * Product 의 GroupBuy 생성 가능 여부를 리턴
	 * @param productId
	 * @return Product 의 GroupBuy 중 GroupBuyStatus 가 PROGRESS 인 GroupBuy 가 존재하면 false
	 */
	public boolean canCreate(final Long productId) {
		return !groupBuyRepository.existsByProductIdAndStatus(productId, GroupBuyStatus.PROGRESS);
	}

	@Transactional
	public RsData<GroupBuy> create(final Product product) {
		if (!canCreate(product.getId())) {
			return RsData.of("F-1", "진행중인 공동구매가 있습니다.", null);
		}

		GroupBuy groupBuy = GroupBuy.builder()
			.product(product)
			.name(product.getName())
			.startDate(LocalDateTime.now())
			// 종료 시간을 현재 시간의 '시'만 가져와서 25시간을 더한 값으로 설정
			.endDate(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusHours(25))
			.status(GroupBuyStatus.PROGRESS)
			.build();

		groupBuyRepository.save(groupBuy);

		return RsData.successOf(groupBuy);
	}

	@Transactional
	public RsData<GroupBuy> extendEndDate(final Long id, final Integer hour) {
		Optional<GroupBuy> unmodifiedGroupBuy = getProgressGroupBuy(id);
		if (unmodifiedGroupBuy.isEmpty()) {
			return RsData.of("F-1", "잘못된 접근입니다.", null);
		}
		GroupBuy groupBuy = unmodifiedGroupBuy.get().toBuilder()
			.endDate(unmodifiedGroupBuy.get().getEndDate().plusHours(hour))
			.build();

		groupBuyRepository.save(groupBuy);

		return RsData.successOf(groupBuy);
	}

	@Transactional
	public GroupBuy updateStatus(GroupBuy originGroupBuy, final GroupBuyStatus status) {
		GroupBuy groupBuy = originGroupBuy.toBuilder()
			.status(status)
			.build();

		// TODO: 멤버 주문 생성 이벤트 publish
		return groupBuyRepository.save(groupBuy);
	}

	/**
	 * GroupBuy 의 endDate가 지나면 status를 PROGRESS -> ORDER로 업데이트
	 */
	@Scheduled(cron = "0 0/5 * * * *")
	@Transactional
	public void checkStatus() {
		log.info("GroupBuyService.checkStatus() Executed");

		List<GroupBuy> groupBuys = groupBuyRepository
			.findByStatusAndEndDateBefore(GroupBuyStatus.PROGRESS, LocalDateTime.now());

		groupBuys.forEach(e -> updateStatus(e, GroupBuyStatus.ORDER));

		// TODO: OrderCreateEvent

		if (groupBuys.size() > 0) {
			groupBuys
				.forEach(e -> log.info("GroupBuy Status Updated to ORDER : GroupBuyId = " + e.getId()));
		}
	}
}
