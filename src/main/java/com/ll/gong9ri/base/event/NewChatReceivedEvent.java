package com.ll.gong9ri.base.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewChatReceivedEvent {
	private Long participantId;
	private String offset;
}
