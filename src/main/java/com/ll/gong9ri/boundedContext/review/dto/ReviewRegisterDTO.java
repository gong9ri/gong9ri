package com.ll.gong9ri.boundedContext.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRegisterDTO {
	private String optionName;
	@NotNull
	private String content;
	@Min(0)
	@Max(10)
	private Integer rate;
}
