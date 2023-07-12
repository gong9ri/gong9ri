package com.ll.gong9ri.boundedContext.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreListDTO {
	private Long storeId;
	private String storeName;
	private Integer productCount;
}
