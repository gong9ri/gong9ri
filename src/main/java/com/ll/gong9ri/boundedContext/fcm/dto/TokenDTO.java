package com.ll.gong9ri.boundedContext.fcm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
	@NotNull
	private String tokenString;
}
