package com.ll.gong9ri.boundedContext.storeChat.repository;

import java.util.List;

import com.ll.gong9ri.boundedContext.storeChat.dto.StoreChatRoomDTO;

public interface StoreChatRoomRepositoryCustom {
	List<StoreChatRoomDTO> getMemberChatRooms(final Long memberId);
}
