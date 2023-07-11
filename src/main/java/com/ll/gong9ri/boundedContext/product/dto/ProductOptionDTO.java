package com.ll.gong9ri.boundedContext.product.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptionDTO {
	private String optionName;
	@Builder.Default
	private List<ProductOptionDetailDTO> optionDetails = new ArrayList<>();
}
