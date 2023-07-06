package com.ll.gong9ri.boundedContext.storeChat.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class StoreChatRoomDTO {
	private StoreChatNoticeDTO room;
	@Builder.Default
	private List<StoreChatMessageDTO> messages = new ArrayList<>();
}
