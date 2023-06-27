package com.ll.gong9ri.boundedContext.chatRoomParticipants.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.boundedContext.chatRoomParticipants.entity.ChatRoomParticipants;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.repository.ChatRoomParticipantsRepository;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.entity.GroupBuyChatRoom;
import com.ll.gong9ri.boundedContext.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomParticipantsService {

	private final ChatRoomParticipantsRepository chatRoomParticipantsRepository;

	@Transactional
	public ChatRoomParticipants createNewParticipant(GroupBuyChatRoom groupBuyChatRoom, Member member) {

		ChatRoomParticipants chatRoomParticipant = ChatRoomParticipants.builder()
			.groupBuyChatRoom(groupBuyChatRoom)
			.member(member)
			.chatOffset("000000000000000000000000")
			.build();

		return chatRoomParticipantsRepository.save(chatRoomParticipant);
	}

	@Transactional
	public void updateOffset(ChatRoomParticipants chatRoomParticipant, String newOffset) {

		chatRoomParticipantsRepository.save(chatRoomParticipant.toBuilder().chatOffset(newOffset).build());
	}

	public Optional<ChatRoomParticipants> findById(Long id) {
		return chatRoomParticipantsRepository.findById(id);
	}
}
