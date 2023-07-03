package com.ll.gong9ri.boundedContext.chatRoomParticipants.controller;

import org.springframework.stereotype.Controller;

import com.ll.gong9ri.boundedContext.chatRoomParticipants.service.ChatRoomParticipantService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatRoomParticipantController {

	private final ChatRoomParticipantService chatRoomParticipantService;
}
