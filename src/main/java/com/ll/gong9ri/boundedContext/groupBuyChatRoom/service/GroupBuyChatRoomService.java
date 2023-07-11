package com.ll.gong9ri.boundedContext.groupBuyChatRoom.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.entity.GroupBuyChatRoom;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.repository.GroupBuyChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupBuyChatRoomService {

	private final GroupBuyChatRoomRepository groupBuyChatRoomRepository;

	@Transactional
	public GroupBuyChatRoom createChatRoom() {
		// TODO: 공동구매 생성 시 채팅방 생성?
		GroupBuyChatRoom chatRoom = GroupBuyChatRoom.builder()
			.name("chatRoomName")
			.build();

		return groupBuyChatRoomRepository.save(chatRoom);
	}

	public GroupBuyChatRoom findById(Long chatRoomId) {
		return groupBuyChatRoomRepository.findById(chatRoomId).orElseThrow();
	}

	@Transactional
	public RsData<GroupBuyChatRoom> createNotice(GroupBuyChatRoom chatRoom, String notice) {
		GroupBuyChatRoom groupBuyChatRoom = chatRoom.toBuilder()
			.notice(notice)
			.build();

		groupBuyChatRoomRepository.save(groupBuyChatRoom);

		return RsData.of("S-1", "공지가 등록되었습니다.", groupBuyChatRoom);
	}
}
