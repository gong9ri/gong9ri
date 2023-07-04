package com.ll.gong9ri.boundedContext.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
	private Long productId;
	private String productName;
	private String optionName;
	private Long memberId;
	private String username;
	private String content;
	private Integer rate;
}
