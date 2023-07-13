package com.ll.gong9ri.base.event;

import com.ll.gong9ri.boundedContext.groupBuyChatRoom.entity.GroupBuyChatRoom;
import com.ll.gong9ri.boundedContext.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventAfterParticipateChatRoom {
	private Member member;
	private GroupBuyChatRoom groupBuyChatRoom;
}
