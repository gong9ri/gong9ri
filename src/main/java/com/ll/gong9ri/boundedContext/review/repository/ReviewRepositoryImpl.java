package com.ll.gong9ri.boundedContext.review.repository;

import static com.ll.gong9ri.boundedContext.member.entity.QMember.*;
import static com.ll.gong9ri.boundedContext.product.entity.QProduct.*;
import static com.ll.gong9ri.boundedContext.review.entity.QReview.*;

import java.util.List;

import com.ll.gong9ri.boundedContext.review.dto.ReviewDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<ReviewDTO> findAllByMemberId(final Long memberId) {
		return queryFactory.select(Projections.constructor(
					ReviewDTO.class,
					review.product.id,
					review.product.name,
					null,
					review.member.id,
					review.member.username,
					review.content
				)
			)
			.from(review)
			.leftJoin(review.product, product)
			.leftJoin(review.member, member)
			.where(
				review.member.id.eq(memberId),
				review.deleteStatus.eq(false)
			)
			.fetch();
	}

	@Override
	public List<ReviewDTO> findAllByProductId(final Long productId) {
		return queryFactory.select(Projections.constructor(
					ReviewDTO.class,
					review.product.id,
					review.product.name,
					null,
					review.member.id,
					review.member.username,
					review.content
				)
			)
			.from(review)
			.leftJoin(review.product, product)
			.leftJoin(review.member, member)
			.where(
				review.product.id.eq(productId),
				review.deleteStatus.eq(false)
			)
			.fetch();

	}

	@Override
	public List<ReviewDTO> findByMemberIdAndProductId(final Long memberId, final Long productId) {
		return queryFactory.select(Projections.constructor(
					ReviewDTO.class,
					review.product.id,
					review.product.name,
					null,
					review.member.id,
					review.member.username,
					review.content
				)
			)
			.from(review)
			.leftJoin(review.product, product)
			.leftJoin(review.member, member)
			.where(
				review.product.id.eq(productId),
				review.member.id.eq(memberId),
				review.deleteStatus.eq(false)
			)
			.fetch();
	}
}
