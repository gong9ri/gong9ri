package com.ll.gong9ri.boundedContext.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDiscountDTO {
	private Long id;
	private Integer headCount;
	@NotNull
	private Integer salePrice;
}
