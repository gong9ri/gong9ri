package com.ll.gong9ri.boundedContext.groupBuyChatMessage.dto;

import java.util.List;

import com.ll.gong9ri.boundedContext.groupBuyChatMessage.entity.GroupBuyChatMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatResponse {
	private List<GroupBuyChatMessage> messages;
	private Long memberId;
}
