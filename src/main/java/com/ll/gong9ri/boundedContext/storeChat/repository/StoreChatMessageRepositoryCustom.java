package com.ll.gong9ri.boundedContext.storeChat.repository;

import java.util.List;

import com.ll.gong9ri.boundedContext.storeChat.dto.StoreChatMessageDTO;

public interface StoreChatMessageRepositoryCustom {
	List<StoreChatMessageDTO> findAllByRoomId(final Long roomId);

	List<StoreChatMessageDTO> findAllByRoomIdAndIdGreaterThan(final Long roomId, final Long offset);
}
