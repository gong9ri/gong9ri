package com.ll.gong9ri.boundedContext.groupBuyChatRoom.repository;

import static com.ll.gong9ri.boundedContext.chatRoomParticipants.entity.QChatRoomParticipant.*;
import static com.ll.gong9ri.boundedContext.groupBuyChatRoom.entity.QGroupBuyChatRoom.*;

import java.util.List;

import com.ll.gong9ri.boundedContext.groupBuyChatRoom.dto.GroupBuyChatRoomDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroupBuyChatRoomRepositoryImpl implements GroupBuyChatRoomRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<GroupBuyChatRoomDto> findAllByMemberId(Long memberId) {
		return queryFactory.select(Projections.constructor(
				GroupBuyChatRoomDto.class,
				groupBuyChatRoom.id,
				groupBuyChatRoom.name))
			.from(groupBuyChatRoom)
			.join(chatRoomParticipant)
			.on(chatRoomParticipant.groupBuyChatRoom.id.eq(groupBuyChatRoom.id))
			.where(
				chatRoomParticipant.member.id.eq(memberId)
			)
			.fetch();

	}
}
