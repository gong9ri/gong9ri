package com.ll.gong9ri.base.event;

import com.ll.gong9ri.boundedContext.storeChat.entity.StoreChatRoom;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventAfterStoreChatMessageCreate {
	private StoreChatRoom room;
	private Long offset;
	private Boolean isMember;
}
