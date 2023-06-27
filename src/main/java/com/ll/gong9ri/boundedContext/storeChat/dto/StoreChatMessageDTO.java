package com.ll.gong9ri.boundedContext.storeChat.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreChatMessageDTO {
	@NotBlank
	private String content;
	private LocalDateTime createDate;
	@NotNull
	private Boolean sentByStore;
}
