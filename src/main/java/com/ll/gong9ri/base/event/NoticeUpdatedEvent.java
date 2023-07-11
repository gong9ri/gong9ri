package com.ll.gong9ri.base.event;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoticeUpdatedEvent {
	private List<String> tokens;
	private String messageHead;
	private String messageContent;
}
