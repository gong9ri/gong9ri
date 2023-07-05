package com.ll.gong9ri.boundedContext.groupBuy.repository;

import static com.ll.gong9ri.boundedContext.groupBuy.entity.QGroupBuy.*;
import static com.ll.gong9ri.boundedContext.groupBuy.entity.QGroupBuyMember.*;

import java.util.List;

import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroupBuyRepositoryImpl implements GroupBuyRepositoryCustom{
	private final JPAQueryFactory queryFactory;

	@Override
	public List<GroupBuyDTO> findAllGroupBuyMemberCount() {
		return queryFactory
			.select(Projections.constructor(
				GroupBuyDTO.class,
				groupBuy.id,
				groupBuy.name,
				groupBuy.product.price,
				groupBuy.product.description,
				groupBuy.product.maxPurchaseNum,
				groupBuy.startDate,
				groupBuy.endDate,
				groupBuyMember.count().intValue()
			))
			.from(groupBuyMember)
			.leftJoin(groupBuyMember.groupBuy, groupBuy)
			.groupBy(groupBuy.id)
			.fetch();
	}
}
