package com.ll.gong9ri.boundedContext.review.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.review.dto.ReviewDTO;
import com.ll.gong9ri.boundedContext.review.dto.ReviewRegisterDTO;
import com.ll.gong9ri.boundedContext.review.entity.Review;
import com.ll.gong9ri.boundedContext.review.repository.ReviewRepository;
import com.ll.gong9ri.boundedContext.review.repository.ReviewRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
	private static final String DEFAULT_VALIDATION_FAIL = "잘못된 접근입니다.";
	private final ReviewRepository reviewRepository;
	private final ReviewRepositoryImpl reviewRepositoryImpl;

	private Optional<Review> findById(final Long id) {
		return reviewRepository.findById(id).filter(e -> !e.getDeleteStatus());
	}

	@Transactional(readOnly = true)
	public RsData<ReviewDTO> getReview(final Long id) {
		return findById(id)
			.map(r -> ReviewDTO.builder()
				.productId(r.getProduct().getId())
				.productName(r.getProduct().getName())
				.optionName(null)
				.memberId(r.getMember().getId())
				.username(r.getMember().getUsername())
				.content(r.getContent())
				.build())
			.map(RsData::successOf)
			.orElseGet(() -> RsData.failOf(null));
	}

	@Transactional(readOnly = true)
	public List<ReviewDTO> getProductReviews(final Long productId) {
		return reviewRepositoryImpl.findAllByProductId(productId);
	}

	@Transactional(readOnly = true)
	public List<ReviewDTO> getMemberReviews(final Long memberId) {
		return reviewRepositoryImpl.findAllByMemberId(memberId);
	}

	@Transactional(readOnly = true)
	public List<ReviewDTO> getMemberProductReviews(final Long memberId, final Long productId) {
		return reviewRepositoryImpl.findByMemberIdAndProductId(memberId, productId);
	}

	public RsData<Review> create(final Member member, final Product product, final ReviewRegisterDTO dto) {
		Review review = Review.builder()
			.product(product)
			.member(member)
			.optionName(dto.getOptionName())
			.content(dto.getContent())
			.rate(dto.getRate())
			.build();

		reviewRepository.save(review);

		return RsData.successOf(review);
	}

	private RsData<Review> validateUpdate(final Long id, final Member member) {
		final Optional<Review> oReview = reviewRepository.findById(id);
		if (oReview.isEmpty()) {
			return RsData.of("F-1", DEFAULT_VALIDATION_FAIL, null);
		}

		final Review review = oReview.get();
		if (!review.getMember().getId().equals(member.getId())) {
			return RsData.of("F-2", DEFAULT_VALIDATION_FAIL, null);
		}

		return RsData.successOf(review);
	}

	public RsData<Review> modify(final Long id, final Member member, final ReviewRegisterDTO dto) {
		RsData<Review> rsReview = validateUpdate(id, member);
		if (rsReview.isFail()) {
			return rsReview;
		}

		Review review = rsReview.getData().toBuilder()
			.content(dto.getContent())
			.rate(dto.getRate())
			.build();

		reviewRepository.save(review);

		return RsData.successOf(review);
	}

	public RsData<Review> delete(final Long id, final Member member) {
		RsData<Review> rsReview = validateUpdate(id, member);
		if (rsReview.isFail()) {
			return rsReview;
		}

		Review review = rsReview.getData().toBuilder()
			.deleteStatus(Boolean.TRUE)
			.build();

		reviewRepository.save(review);

		return RsData.successOf(review);
	}
}
