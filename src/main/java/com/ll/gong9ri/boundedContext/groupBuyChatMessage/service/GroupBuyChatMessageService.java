package com.ll.gong9ri.boundedContext.groupBuyChatMessage.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ll.gong9ri.boundedContext.groupBuyChatMessage.entity.GroupBuyChatMessage;
import com.ll.gong9ri.boundedContext.groupBuyChatMessage.repository.GroupBuyChatMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupBuyChatMessageService {

	private final GroupBuyChatMessageRepository groupBuyChatMessageRepository;

	public GroupBuyChatMessage sendChat(String content){
		GroupBuyChatMessage groupBuyChatMessage = GroupBuyChatMessage.builder()
			.content(content)
			.build();

		groupBuyChatMessageRepository.save(groupBuyChatMessage);

		return groupBuyChatMessage;
	}

	public Optional<GroupBuyChatMessage> findById(String id){
		return groupBuyChatMessageRepository.findById(id);
	}
}
