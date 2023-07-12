package com.ll.gong9ri.boundedContext.product.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOptionDTO {
	@Builder.Default
	private List<ProductOptionDetailDTO> optionDetails = new ArrayList<>();
}
