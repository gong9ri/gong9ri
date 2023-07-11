package com.ll.gong9ri.boundedContext.product.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ll.gong9ri.boundedContext.product.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDTO {
	private Long id;
	private String name;
	@Builder.Default
	private List<String> images = new ArrayList<>();
	private LocalDateTime createDate;
	private String optionName;
	@Builder.Default
	private List<ProductOptionDetailDTO> options = new ArrayList<>();
	@Builder.Default
	private List<ProductDiscountDTO> discounts = new ArrayList<>();

	public static ProductDetailDTO of(final Product product) {
		return ProductDetailDTO.builder()
			.id(product.getId())
			.name(product.getName())
			.images(product.getImages()
				.stream()
				.map(e -> String.valueOf(e.getId())) // TODO: image url
				.toList())
			.createDate(product.getCreateDate())
			.optionName(product.getOptionName())
			.options(product.getProductOptions()
				.stream()
				.map(e -> ProductOptionDetailDTO.builder()
					.id(e.getId())
					.optionDetail(e.getOptionDetail())
					.build())
				.toList())
			.discounts(product.getProductDiscounts()
				.stream().map(e -> ProductDiscountDTO.builder()
					.headCount(e.getHeadCount())
					.salePrice(e.getSalePrice())
					.build())
				.toList())
			.build();
	}

}
