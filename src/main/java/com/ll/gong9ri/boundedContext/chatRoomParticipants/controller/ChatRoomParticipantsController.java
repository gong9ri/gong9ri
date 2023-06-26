package com.ll.gong9ri.boundedContext.chatRoomParticipants.controller;

import org.springframework.stereotype.Controller;

import com.ll.gong9ri.boundedContext.chatRoomParticipants.service.ChatRoomParticipantsService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatRoomParticipantsController {

	private final ChatRoomParticipantsService chatRoomParticipantsService;
}
