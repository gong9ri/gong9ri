package com.ll.gong9ri.boundedContext.groupBuyChatRoom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoticeDto {
	@NotBlank
	private String notice;
}
