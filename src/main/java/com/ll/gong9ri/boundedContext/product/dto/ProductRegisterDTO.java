package com.ll.gong9ri.boundedContext.product.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ll.gong9ri.boundedContext.product.entity.Product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductRegisterDTO {
	@NotEmpty(message = "제목을 입력해 주세요.")
	private String name;
	@NotNull(message="가격은 필수항목 입니다.")
	@Min(value = 0, message = "가격은 0보다 크거나 같아야 합니다.")
	private Integer price;
	@NotBlank(message="내용을 입력해 주세요.")
	private String description;
	@Builder.Default
	private Integer maxPurchaseNum = 10;
	@Builder.Default
	private List<MultipartFile> images = new ArrayList<>();

	public Product toEntity() {
		return Product.builder()
			.name(name)
			.price(price)
			.description(description)
			.maxPurchaseNum(maxPurchaseNum)
			.build();
	}
}
