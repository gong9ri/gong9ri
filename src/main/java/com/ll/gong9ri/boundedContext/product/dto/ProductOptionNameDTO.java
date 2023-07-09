package com.ll.gong9ri.boundedContext.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptionNameDTO {
	private Long id;
	@NotBlank
	private String optionOneName;
	@NotBlank
	private String optionTwoName;
}
