package com.ll.gong9ri.boundedContext.order.dto;

import java.util.HashMap;
import java.util.Map;

import com.ll.gong9ri.boundedContext.product.entity.ProductOption;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDTO {
	@NotNull
	private Long productId;
	private Integer salePrice;
	@Builder.Default
	private Map<ProductOption, Integer> options = new HashMap<>();
	@NotBlank
	private String recipient;
	@NotBlank
	private String mainAddress;
	private String subAddress;
}
