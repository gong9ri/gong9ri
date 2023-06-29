package com.ll.gong9ri.boundedContext.storeChat.repository;

import java.util.List;

import com.ll.gong9ri.boundedContext.storeChat.dto.StoreChatRoomDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StoreChatRoomRepositoryImpl implements StoreChatRoomRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<StoreChatRoomDTO> getMemberChatRooms(Long memberId) {
		return null;
	}
}
