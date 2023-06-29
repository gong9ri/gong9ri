package com.ll.gong9ri.boundedContext.storeChat.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.boundedContext.storeChat.service.StoreChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/store/chat")
@RequiredArgsConstructor
public class StoreChatController {
	private final StoreChatService storeChatService;
	private final Rq rq;

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/")
	public String list(Model model) {
		model.addAttribute("rooms", storeChatService.getMemberChatRooms(rq.getMember().getId()));

		return "usr/store/chat/list";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{roomId}")
	public String chatRoom(Model model, @PathVariable Long roomId) {

		return "usr/store/chatroom";
	}
}
