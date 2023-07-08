package com.ll.gong9ri.boundedContext.groupBuy.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyDetailDTO;
import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyListDTO;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyMemberRole;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;
import com.ll.gong9ri.boundedContext.groupBuy.entity.QGroupBuy;
import com.ll.gong9ri.boundedContext.groupBuy.entity.QGroupBuyMember;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GroupBuyRepositoryImpl implements GroupBuyRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<GroupBuyListDTO> getAllGroupBuyListDTOInProgress() {
		QGroupBuy groupBuy = QGroupBuy.groupBuy;
		QGroupBuyMember groupBuyMember = QGroupBuyMember.groupBuyMember;

		Expression<Integer> currentMemberCount = JPAExpressions
			.select(groupBuyMember.count().intValue())
			.from(groupBuyMember)
			.where(groupBuyMember.groupBuy.id.eq(groupBuy.id)
				.and(groupBuyMember.role.ne(GroupBuyMemberRole.STORE)));

		return queryFactory
			.select(Projections.constructor(
				GroupBuyListDTO.class,
				groupBuy.id,
				groupBuy.name,
				groupBuy.product.price,
				groupBuy.startDate,
				groupBuy.endDate,
				groupBuy.status,
				currentMemberCount
			))
			.from(groupBuyMember)
			.where(
				groupBuy.status.eq(GroupBuyStatus.PROGRESS),
				groupBuyMember.role.ne(GroupBuyMemberRole.STORE)
			)
			.leftJoin(groupBuyMember.groupBuy, groupBuy)
			.groupBy(groupBuy.id)
			.fetch();
	}

	@Override
	public List<GroupBuyListDTO> getAllGroupBuyListDTO() {
		QGroupBuy groupBuy = QGroupBuy.groupBuy;
		QGroupBuyMember groupBuyMember = QGroupBuyMember.groupBuyMember;

		Expression<Integer> currentMemberCount = JPAExpressions
			.select(groupBuyMember.count().intValue())
			.from(groupBuyMember)
			.where(groupBuyMember.groupBuy.id.eq(groupBuy.id)
				.and(groupBuyMember.role.ne(GroupBuyMemberRole.STORE)));

		return queryFactory
			.select(Projections.constructor(
				GroupBuyListDTO.class,
				groupBuy.id,
				groupBuy.name,
				groupBuy.product.price,
				groupBuy.startDate,
				groupBuy.endDate,
				groupBuy.status,
				currentMemberCount
			))
			.from(groupBuyMember)
			.where(groupBuyMember.role.ne(GroupBuyMemberRole.STORE))
			.leftJoin(groupBuyMember.groupBuy, groupBuy)
			.groupBy(groupBuy.id)
			.fetch();
	}

	public GroupBuyDetailDTO getGroupBuyDetailDTO(Member member){
		QGroupBuy groupBuy = QGroupBuy.groupBuy;
		QGroupBuyMember groupBuyMember = QGroupBuyMember.groupBuyMember;

		Expression<Integer> currentMemberCount = JPAExpressions
			.select(groupBuyMember.count().intValue())
			.from(groupBuyMember)
			.where(groupBuyMember.groupBuy.id.eq(groupBuy.id)
				.and(groupBuyMember.role.ne(GroupBuyMemberRole.STORE)));

		Expression<Boolean> isParticipate = JPAExpressions
			.select(groupBuyMember.id)
				.from(groupBuyMember)
				.where(groupBuyMember.member.id.eq(member.getId())
					.and(groupBuyMember.groupBuy.id.eq(groupBuy.id)))
				.exists();

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
				currentMemberCount,
				isParticipate
			))
			.from(groupBuy)
			.orderBy(groupBuy.startDate.desc())
			.fetchFirst();
	}
}
