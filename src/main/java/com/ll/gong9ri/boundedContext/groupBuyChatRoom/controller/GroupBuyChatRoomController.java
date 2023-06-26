package com.ll.gong9ri.boundedContext.groupBuyChatRoom.controller;

import org.springframework.stereotype.Controller;

import com.ll.gong9ri.boundedContext.groupBuyChatRoom.service.GroupBuyChatRoomService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GroupBuyChatRoomController {

	private final GroupBuyChatRoomService groupBuyChatRoomService;
}
