package com.ll.gong9ri.boundedContext.groupBuy.repository;

import static com.ll.gong9ri.boundedContext.groupBuy.entity.QGroupBuy.*;
import static com.ll.gong9ri.boundedContext.groupBuy.entity.QGroupBuyMember.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyDetailDTO;
import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyListDTO;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyMemberRole;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GroupBuyRepositoryImpl implements GroupBuyRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	private Expression<Boolean> isParticipate(final Long memberId) {
		return memberId == null ? Expressions.FALSE : JPAExpressions
			.select(groupBuyMember.id)
			.from(groupBuyMember)
			.where(groupBuyMember.member.id.eq(memberId)
				.and(groupBuyMember.groupBuy.id.eq(groupBuy.id))
			)
			.exists();
	}

	private BooleanExpression eqStatus(final GroupBuyStatus status) {
		return status == null ? null : groupBuy.status.eq(status);
	}

	@Override
	public List<GroupBuyListDTO> searchGroupBuyListDTO(
		final GroupBuyStatus groupBuyStatus,
		final Long memberId
	) {
		return queryFactory
			.select(Projections.constructor(
				GroupBuyListDTO.class,
				groupBuy.id,
				groupBuy.name,
				groupBuy.product.price,
				groupBuy.startDate,
				groupBuy.endDate,
				groupBuy.status,
				groupBuy.groupBuyMembers.size(),
				groupBuy.nextHeadCount,
				groupBuy.currentSalePrice,
				groupBuy.nextSalePrice,
				isParticipate(memberId)
			))
			.from(groupBuyMember)
			.where(
				eqStatus(groupBuyStatus),
				groupBuyMember.role.ne(GroupBuyMemberRole.STORE)
			)
			.leftJoin(groupBuyMember.groupBuy, groupBuy)
			.groupBy(groupBuy.id)
			.fetch();
	}

	public GroupBuyDetailDTO getGroupBuyDetailDTO(final Long id, final Long memberId) {
		return queryFactory
			.select(Projections.constructor(GroupBuyDetailDTO.class,
				groupBuy.id,
				groupBuy.name,
				groupBuy.product.price,
				groupBuy.product.description,
				groupBuy.product.maxPurchaseNum,
				groupBuy.startDate,
				groupBuy.endDate,
				groupBuy.status,
				groupBuy.groupBuyMembers.size(),
				groupBuy.nextHeadCount,
				groupBuy.currentSalePrice,
				groupBuy.nextSalePrice,
				isParticipate(memberId)
			))
			.from(groupBuy)
			.where(groupBuy.id.eq(id))
			.fetchFirst();
	}
}
