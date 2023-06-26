package com.ll.gong9ri.boundedContext.chatRoomParticipants.service;

import org.springframework.stereotype.Service;

import com.ll.gong9ri.boundedContext.chatRoomParticipants.repository.ChatRoomParticipantsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomParticipantsService {

	private final ChatRoomParticipantsRepository chatRoomParticipantsRepository;
}
