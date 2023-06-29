package com.ll.gong9ri.boundedContext.storeChat.controller;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import com.ll.gong9ri.boundedContext.store.service.StoreService;
import com.ll.gong9ri.boundedContext.storeChat.service.StoreChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/manage/store/chat")
@RequiredArgsConstructor
public class ManageStoreChatController {
	private static final String DEFAULT_ERROR_MESSAGE = "관리자에게 문의하세요.";
	private final StoreService storeService;
	private final StoreChatService storeChatService;
	private final Rq rq;

	@PreAuthorize("isAuthenticated() and hasAuthority('store')")
	@GetMapping("/")
	public String list(Model model) {
		// todo: has authority
		Optional<Store> oStore = storeService.findByMemberId(rq.getMember().getId());
		if (oStore.isEmpty()) {
			return rq.redirectWithErrorMsg(DEFAULT_ERROR_MESSAGE, "/");
		}

		model.addAttribute("rooms", storeChatService.getStoreChatRooms(oStore.get().getId()));

		return "usr/store/chat/list";
	}

	@PreAuthorize("isAuthenticated() and hasAuthority('store')")
	@GetMapping("/{roomId}")
	public String chatRoom(Model model, @PathVariable Long roomId) {

		return "usr/store/chat/room";
	}
}
