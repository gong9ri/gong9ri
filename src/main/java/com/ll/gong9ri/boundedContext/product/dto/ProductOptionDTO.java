package com.ll.gong9ri.boundedContext.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptionDTO {
	private String optionOne;
	private String optionTwo;
	@Builder.Default
	private List<ProductOptionNameDTO> optionNames = new ArrayList<>();
}
