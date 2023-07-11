package com.ll.gong9ri.boundedContext.product.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ll.gong9ri.boundedContext.product.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
	private List<MultipartFile> images;

	public Product toEntity() {
		return Product.builder()
			.name(name)
			.price(price)
			.description(description)
			.maxPurchaseNum(maxPurchaseNum)
			.build();
	}
}
