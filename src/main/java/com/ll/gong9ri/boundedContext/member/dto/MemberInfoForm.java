package com.ll.gong9ri.boundedContext.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInfoForm {
	@NotBlank
	private String nickname;
}
