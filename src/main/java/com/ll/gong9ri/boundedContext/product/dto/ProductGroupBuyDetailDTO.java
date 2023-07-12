package com.ll.gong9ri.boundedContext.product.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductGroupBuyDetailDTO {
	private Long id;
	private String name;
	@Builder.Default
	private List<String> images = new ArrayList<>();
	private LocalDateTime createDate;
	private String optionName;
	@Builder.Default
	private List<ProductOptionDetailDTO> options = new ArrayList<>();
	private Integer originPrice;
	private Integer salePrice;

	public static ProductGroupBuyDetailDTO of(final OrderInfo orderInfo) {
		return ProductGroupBuyDetailDTO.builder()
			.id(orderInfo.getProduct().getId())
			.name(orderInfo.getProduct().getName())
			.images(orderInfo.getProduct().getImages()
				.stream()
				.map(e -> String.valueOf(e.getId())) // TODO: image url
				.toList())
			.createDate(orderInfo.getProduct().getCreateDate())
			.optionName(orderInfo.getProduct().getOptionName())
			.options(orderInfo.getProduct().getProductOptions()
				.stream()
				.map(e -> ProductOptionDetailDTO.builder()
					.id(e.getId())
					.optionDetail(e.getOptionDetail())
					.build())
				.toList())
			.originPrice(orderInfo.getProduct().getPrice())
			.salePrice(orderInfo.getPrice())
			.build();
	}
}
