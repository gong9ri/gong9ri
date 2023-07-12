package com.ll.gong9ri.boundedContext.store.dto;

import java.util.ArrayList;
import java.util.List;

import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreHomeDTO {
	private Long storeId;
	@NotBlank
	private String storeName;
	@Builder.Default
	private List<ProductDTO> products = new ArrayList<>();
}
