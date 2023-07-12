package com.ll.gong9ri.base.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventAfterNewChatReceived {
	private Long participantId;
	private String offset;
}
