package com.ll.gong9ri.boundedContext.groupBuy.repository;

import static com.ll.gong9ri.boundedContext.groupBuy.entity.QGroupBuy.*;
import static com.ll.gong9ri.boundedContext.groupBuy.entity.QGroupBuyMember.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyDetailDTO;
import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyListDTO;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
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
	public Page<GroupBuyListDTO> searchGroupBuyListDTO(
		final GroupBuyStatus status,
		final Long memberId,
		Pageable pageable
	) {
		JPAQuery<GroupBuyListDTO> query = queryFactory
			.select(Projections.constructor(
				GroupBuyListDTO.class,
				groupBuy.id,
				groupBuy.name,
				groupBuy.product.price,
				groupBuy.startDate,
				groupBuy.endDate,
				groupBuy.status,
				groupBuy.groupBuyMembers.size().subtract(Expressions.constant(1)),
				groupBuy.nextHeadCount,
				groupBuy.currentSalePrice,
				groupBuy.nextSalePrice,
				isParticipate(memberId)
			))
			.from(groupBuy)
			.leftJoin(groupBuy.groupBuyMembers, groupBuyMember)
			.where(eqStatus(status), groupBuyMember.member.id.eq(memberId))
			.groupBy(groupBuy.id);

		if (status != null) {
			query.where(groupBuy.status.eq(status));
		}

		if (memberId != null) {
			query.having(groupBuyMember.member.id.eq(memberId));
		}

		if (pageable.isPaged()) {
			query.offset(pageable.getOffset()).limit(pageable.getPageSize());
		}

		long totalCount = query.fetchCount();

		List<GroupBuyListDTO> resultList = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return new PageImpl<>(resultList, pageable, totalCount);
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
				groupBuy.groupBuyMembers.size().subtract(Expressions.constant(1)),
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
