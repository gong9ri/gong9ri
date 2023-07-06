package com.ll.gong9ri.boundedContext.product.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ProductOptionDTO {
	private String optionOne;
	private String optionTwo;
	@Builder.Default
	private List<ProductOptionNameDTO> optionNames = new ArrayList<>();
}
