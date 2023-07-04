package com.ll.gong9ri.boundedContext.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
