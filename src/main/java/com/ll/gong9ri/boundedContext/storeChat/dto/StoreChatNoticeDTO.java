package com.ll.gong9ri.boundedContext.storeChat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreChatNoticeDTO {
	private Long roomId;
	private String senderName;
	private Long chatOffset;
	private Integer noticeCount;
}
