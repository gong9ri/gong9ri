package com.ll.gong9ri.boundedContext.groupBuyChatMessage.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.ll.gong9ri.boundedContext.groupBuyChatMessage.entity.GroupBuyChatMessage;
import com.ll.gong9ri.boundedContext.groupBuyChatMessage.repository.GroupBuyChatMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupBuyChatMessageService {

	private final SimpMessagingTemplate messagingTemplate;
	private final GroupBuyChatMessageRepository groupBuyChatMessageRepository;

	public GroupBuyChatMessage sendChat(String content, String roomId){
		GroupBuyChatMessage groupBuyChatMessage = GroupBuyChatMessage.builder()
			.content(content)
			.chatRoomId(roomId)
			.createDate(LocalDateTime.now())
			.build();

		groupBuyChatMessageRepository.save(groupBuyChatMessage);

		messagingTemplate.convertAndSend("/topic/" + roomId, groupBuyChatMessage);

		return groupBuyChatMessage;
	}

	public Optional<GroupBuyChatMessage> findById(String id){
		return groupBuyChatMessageRepository.findById(id);
	}

	public List<GroupBuyChatMessage> getChatMessagesByRoomId(Long roomId) {

		return groupBuyChatMessageRepository.findByChatRoomId(String.valueOf(roomId));
	}
}
