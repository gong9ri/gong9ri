package com.ll.gong9ri.boundedContext.fcm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenDTO {
	@NotNull
	private String tokenString;
}
