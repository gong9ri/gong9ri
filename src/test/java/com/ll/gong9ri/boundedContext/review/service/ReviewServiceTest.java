package com.ll.gong9ri.boundedContext.review.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.review.dto.ReviewRegisterDTO;
import com.ll.gong9ri.boundedContext.review.entity.Review;
import com.ll.gong9ri.boundedContext.store.entity.Store;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class ReviewServiceTest {
	@Autowired
	private ReviewService reviewService;

	private Member member, storeMember;
	private Store store;
	private Product product;

	@BeforeEach
	void beforeEach() {
		member = Member.builder()
			.id(2344532L)
			.username("sfsdg")
			.build();
		storeMember = Member.builder()
			.id(244532L)
			.username("sfsdg")
			.build();
		store = Store.builder()
			.member(storeMember)
			.name("sdfsdf")
			.id(45690L)
			.build();
		product = Product.builder()
			.id(234234L)
			.store(store)
			.name("345uew")
			.build();
	}

	@Test
	@DisplayName("create crud test")
	void reviewCrud() throws Exception {
		ReviewRegisterDTO dto = ReviewRegisterDTO.builder()
			.content("ggoooD R)==))eivw")
			.rate(1)
			.build();
		RsData<Review> rsReview = reviewService.create(member, product, dto);
		assertThat(rsReview.isSuccess()).isTrue();

		assertThat(reviewService.getReview(rsReview.getData().getId())).isNotNull();
		ReviewRegisterDTO mDto = ReviewRegisterDTO.builder()
			.content("ijio")
			.rate(2)
			.build();

		RsData<Review> rsMReview = reviewService.modify(rsReview.getData().getId(), member, mDto);
		assertThat(rsMReview.getData().getContent()).isEqualTo(rsReview.getData().getContent());

		reviewService.delete(rsReview.getData().getId(), member);
		assertThat(reviewService.getReview(rsMReview.getData().getId()).isFail()).isTrue();
	}
}
