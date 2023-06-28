package com.ll.gong9ri.boundedContext.groupBuyChatRoom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.service.ChatRoomParticipantService;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.entity.GroupBuyChatRoom;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.service.GroupBuyChatRoomService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/groupbuy")
@RequiredArgsConstructor
public class GroupBuyChatRoomController {

	private final GroupBuyChatRoomService groupBuyChatRoomService;
	private final ChatRoomParticipantService chatRoomParticipantService;
	private final Rq rq;

	@GetMapping("/make")
	public String create(){
		groupBuyChatRoomService.createChatRoom();

		return rq.redirectWithMsg("groupbuy/1","");
	}

	@GetMapping("/{chatRoomId}")
	public String showChatRoom(@PathVariable Long chatRoomId, Model model){

		GroupBuyChatRoom chatRoom = groupBuyChatRoomService.findById(chatRoomId);
		model.addAttribute("chatRoom", chatRoom);

		chatRoomParticipantService.createNewParticipant(chatRoom, rq.getMember());

		return "groupBuy/roomDetail";
	}
}
