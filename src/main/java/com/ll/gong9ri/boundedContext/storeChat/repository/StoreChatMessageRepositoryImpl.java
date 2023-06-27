package com.ll.gong9ri.boundedContext.storeChat.repository;

import static com.ll.gong9ri.boundedContext.storeChat.entity.QStoreChatMessage.*;
import static com.ll.gong9ri.boundedContext.storeChat.entity.QStoreChatRoom.*;

import java.util.List;

import com.ll.gong9ri.boundedContext.storeChat.dto.StoreChatMessageDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StoreChatMessageRepositoryImpl implements StoreChatMessageRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<StoreChatMessageDTO> findAllByRoomId(final Long roomId) {
		return jpaQueryFactory.select(Projections.constructor(
				StoreChatMessageDTO.class,
				storeChatMessage.content,
				storeChatMessage.createDate,
				storeChatMessage.sentByStore
			))
			.from(storeChatMessage)
			.leftJoin(storeChatMessage.storeChatRoom, storeChatRoom)
			.where(storeChatMessage.storeChatRoom.id.eq(roomId))
			.fetch();
	}

	@Override
	public List<StoreChatMessageDTO> findAllByRoomIdAndIdGreaterThan(final Long roomId, final Long offset) {
		return jpaQueryFactory.select(Projections.constructor(
				StoreChatMessageDTO.class,
				storeChatMessage.content,
				storeChatMessage.createDate,
				storeChatMessage.sentByStore
			))
			.from(storeChatMessage)
			.leftJoin(storeChatMessage.storeChatRoom, storeChatRoom)
			.where(
				storeChatMessage.storeChatRoom.id.eq(roomId),
				storeChatMessage.id.gt(offset)
			)
			.fetch();
	}
}
