package com.ll.gong9ri.boundedContext.review.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.service.ProductService;
import com.ll.gong9ri.boundedContext.review.dto.ReviewDTO;
import com.ll.gong9ri.boundedContext.review.dto.ReviewRegisterDTO;
import com.ll.gong9ri.boundedContext.review.entity.Review;
import com.ll.gong9ri.boundedContext.review.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
	private final ProductService productService;
	private final ReviewService reviewService;
	private final Rq rq;

	private RsData<Product> validateProduct(final Long productId) {
		return productService.getProduct(productId)
			.map(RsData::successOf)
			.orElse(RsData.of("F-1", "잘못된 접근입니다.", null));
	}

	@GetMapping("/product/{productId}")
	public String productReviews(@PathVariable Long productId, Model model) {
		final RsData<Product> rsProduct = validateProduct(productId);
		if (rsProduct.isFail()) {
			return rq.historyBack(rsProduct);
		}

		List<ReviewDTO> reviews = reviewService.getProductReviews(productId);
		model.addAttribute("reviews", reviews);
		return "usr/review/list";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/product/{productId}")
	public String registerReview(@PathVariable Long productId, @Valid ReviewRegisterDTO dto) {
		final RsData<Product> rsProduct = validateProduct(productId);
		if (rsProduct.isFail()) {
			return rq.historyBack(rsProduct);
		}

		final RsData<Review> rsReview = reviewService.create(rq.getMember(), rsProduct.getData(), dto);
		if (rsReview.isFail()) {
			return rq.historyBack(rsReview);
		}

		return rq.redirectWithMsg("/review/" + rsReview.getData().getId(), rsReview);
	}

	@PreAuthorize("isAuthenticated()")
	@PutMapping("/{reviewId}")
	public String updateReview(@PathVariable Long reviewId, @Valid ReviewRegisterDTO dto) {
		final RsData<Review> rsReview = reviewService.modify(reviewId, rq.getMember(), dto);
		if (rsReview.isFail()) {
			return rq.historyBack(rsReview);
		}

		return rq.redirectWithMsg("/review/" + reviewId, rsReview);
	}

	@PreAuthorize("isAuthenticated()")
	@PatchMapping("/{reviewId}")
	public String deleteReview(@PathVariable Long reviewId) {
		final RsData<Review> rsReview = reviewService.delete(reviewId, rq.getMember());
		if (rsReview.isFail()) {
			return rq.historyBack(rsReview);
		}

		return rq.redirectWithMsg("/me/review", rsReview);
	}
}
