package com.ll.gong9ri.boundedContext.review.repository;

import java.util.List;

import com.ll.gong9ri.boundedContext.review.dto.ReviewDTO;

public interface ReviewRepositoryCustom {
	List<ReviewDTO> findAllByMemberId(final Long memberId);

	List<ReviewDTO> findAllByProductId(final Long productId);

	List<ReviewDTO> findByMemberIdAndProductId(final Long memberId, final Long productId);
}
