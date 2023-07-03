package com.ll.gong9ri.boundedContext.groupBuyChatRoom.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.boundedContext.groupBuyChatRoom.entity.GroupBuyChatRoom;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.repository.GroupBuyChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupBuyChatRoomService {

	private final GroupBuyChatRoomRepository groupBuyChatRoomRepository;

	@Transactional
	public GroupBuyChatRoom createChatRoom(){
		GroupBuyChatRoom chatRoom = GroupBuyChatRoom.builder()
			.build();

		return groupBuyChatRoomRepository.save(chatRoom);
	}
	public GroupBuyChatRoom findById(Long chatRoomId) {
		return groupBuyChatRoomRepository.findById(chatRoomId).orElseThrow();
	}
}
