package com.ll.gong9ri.boundedContext.store.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreJoinDTO {
	@NotBlank
	private String username;
	@NotBlank
	private String storeName;
	@NotBlank
	private String password;
}
