package com.ll.gong9ri.boundedContext.member.entity;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProviderTypeCode {
	GONG9RI("GONG9RI"),
	KAKAO("KAKAO"),
	NAVER("NAVER"),
	GOOGLE("GOOGLE");

	private final String code;

	public static ProviderTypeCode codeOf(final String rawCode) {
		return Arrays.stream(values()).filter(e -> e.getCode().equals(rawCode)).findFirst().orElse(GONG9RI);
	}
}
