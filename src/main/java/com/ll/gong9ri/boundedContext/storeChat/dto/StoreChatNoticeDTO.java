package com.ll.gong9ri.boundedContext.storeChat.dto;

import com.ll.gong9ri.boundedContext.storeChat.entity.StoreChatRoom;

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

	public static StoreChatNoticeDTO storeOf(final StoreChatRoom room) {
		return StoreChatNoticeDTO.builder()
			.roomId(room.getId())
			.senderName(room.getMember().getUsername())
			.chatOffset(room.getStoreChatOffset())
			.noticeCount(room.getStoreNoticeCount())
			.build();
	}

	public static StoreChatNoticeDTO memberOf(final StoreChatRoom room) {
		return StoreChatNoticeDTO.builder()
			.roomId(room.getId())
			.senderName(room.getStore().getName())
			.chatOffset(room.getMemberChatOffset())
			.noticeCount(room.getMemberNoticeCount())
			.build();
	}
}
