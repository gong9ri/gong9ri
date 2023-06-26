package com.ll.gong9ri.boundedContext.groupBuy.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupBuyStatus {
	// TODO: names?
	WAIT,
	START,
	PROGRESS,
	EXPIRE,
	DONE;
}
