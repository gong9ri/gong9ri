package com.ll.gong9ri.base.event;

import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventAfterGroupBuyCreated {
	private GroupBuy groupBuy;
}
