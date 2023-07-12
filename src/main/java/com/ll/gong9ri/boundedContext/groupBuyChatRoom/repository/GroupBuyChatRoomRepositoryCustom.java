package com.ll.gong9ri.boundedContext.groupBuyChatRoom.repository;

import java.util.List;

import com.ll.gong9ri.boundedContext.groupBuyChatRoom.dto.GroupBuyChatRoomDto;

public interface GroupBuyChatRoomRepositoryCustom {
	List<GroupBuyChatRoomDto> findAllByMemberId(Long memberId);
}
