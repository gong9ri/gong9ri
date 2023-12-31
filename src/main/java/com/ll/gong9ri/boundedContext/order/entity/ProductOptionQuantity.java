package com.ll.gong9ri.boundedContext.order.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionQuantity {
	private Long optionId;
	private String optionDetail;
	private Integer quantity;
}