package com.ll.gong9ri.boundedContext.product.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;

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
	private String optionOne;
	private String optionTwo;
	/**
	 * when optionOne only Exist, "optionOneName, null"
	 * when optionOne, Two Exist, "optionOneName, Lists of optionTwoNames"
	 */
	private Map<String, List<String>> options;
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
			.optionOne(product.getOptionOne())
			.optionTwo(product.getOptionTwo())
			.options(product.getProductOptions()
				.stream()
				.collect(Collectors.groupingBy(ProductOption::getOptionOneName,
					Collectors.mapping(ProductOption::getOptionTwoName, Collectors.toList())))
			)
			.discounts(product.getProductDiscounts()
				.stream().map(e -> ProductDiscountDTO.builder()
					.headCount(e.getHeadCount())
					.salePrice(e.getSalePrice())
					.build())
				.toList())
			.build();
	}
}
