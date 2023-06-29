package com.ll.gong9ri.boundedContext.storeChat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreChatRoomDTO {
	private Long roomId;
	private Long lastSeenOffset;
	private Long notReadCount;
}
