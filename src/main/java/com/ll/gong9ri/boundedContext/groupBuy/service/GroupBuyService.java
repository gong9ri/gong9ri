package com.ll.gong9ri.boundedContext.groupBuy.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;
import com.ll.gong9ri.boundedContext.groupBuy.repository.GroupBuyRepository;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupBuyService {
	private final GroupBuyRepository groupBuyRepository;
	private final ProductRepository productRepository;

	@Transactional
	public RsData<GroupBuy> createGroupBuy(Long productId, Member member){
		Product product = productRepository.findById(productId).orElse(null);

		if(product == null){
			return RsData.of("F-1", "존재하지 않는 상품입니다.");
		}
		GroupBuy groupBuy = GroupBuy.builder()
			.product(product)
			.name(product.getName())
			.startDate(LocalDateTime.now())
			.endDate(LocalDateTime.now().plusDays(1)) // 시작시간으로부터 하루 뒤
			.status(GroupBuyStatus.START)
			.build();

		groupBuy.addGroupBuyMember(member);

		groupBuyRepository.save(groupBuy);

		return RsData.successOf(groupBuy);
	}
}
