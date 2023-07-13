package com.ll.gong9ri.base.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventAfterUpdateToken {
	private String tokenString;
	private Long memberId;
}
