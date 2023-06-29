package com.ll.gong9ri.boundedContext.storeChat.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.boundedContext.store.service.StoreService;
import com.ll.gong9ri.boundedContext.storeChat.service.StoreChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class StoreChatController {
	private final StoreService storeService;
	private final StoreChatService storeChatService;
	private final Rq rq;

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/")
	public String list(Model model) {
		// final List<StoreChatRoomDTO> rooms = storeChatService.getMemberChatRooms(rq.getMember().getId());
		// model.addAttribute("rooms", rooms);

		return "usr/store/chat/list";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{roomId}")
	public String chatRoom(Model model, @PathVariable Long roomId) {
		// RsData<StoreChatRoomDTO> rsRoom = storeChatService.getRoom(roomId, rq.getMember().getId());
		// List<?> messages = storeChatService.getAllMessages(roomId);
		// if (rsStore.isFail()) {
		// 	return rq.historyBack(rsStore);
		// }
		//
		// model.addAttribute("store", rsStore.getData());

		return "usr/store/chat/room";
	}
}
