package com.ll.gong9ri.boundedContext.store.dto;

import java.util.ArrayList;
import java.util.List;

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
	@NotBlank
	private String storeName;
	// TODO: productThumbnailDTO
	@Builder.Default
	private List<?> products = new ArrayList<>();
}
