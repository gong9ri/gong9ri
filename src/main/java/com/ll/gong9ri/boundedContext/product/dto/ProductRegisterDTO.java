package com.ll.gong9ri.boundedContext.product.dto;

import com.ll.gong9ri.boundedContext.product.entity.Product;

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
public class ProductRegisterDTO {
	@NotBlank
	private String name;
	@NotNull
	private Integer price;
	@NotBlank
	private String description;
	@Builder.Default
	private Integer maxPurchaseNum = 0;

	public Product toEntity() {
		return Product.builder()
			.name(name)
			.price(price)
			.description(description)
			.maxPurchaseNum(maxPurchaseNum)
			.build();
	}
}
