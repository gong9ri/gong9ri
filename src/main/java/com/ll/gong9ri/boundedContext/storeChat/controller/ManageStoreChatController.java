package com.ll.gong9ri.boundedContext.storeChat.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import com.ll.gong9ri.boundedContext.store.service.StoreService;
import com.ll.gong9ri.boundedContext.storeChat.dto.StoreChatMessageDTO;
import com.ll.gong9ri.boundedContext.storeChat.dto.StoreChatNoticeDTO;
import com.ll.gong9ri.boundedContext.storeChat.entity.StoreChatRoom;
import com.ll.gong9ri.boundedContext.storeChat.service.StoreChatService;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/manage/store/chat")
@RequiredArgsConstructor
public class ManageStoreChatController {
	private static final String DEFAULT_ERROR_MESSAGE = "연결된 스토어가 없습니다. 관리자에게 문의하세요.";
	private final StoreService storeService;
	private final StoreChatService storeChatService;
	private final Rq rq;

	@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_STORE')")
	@GetMapping("/")
	public String list(Model model) {
		final Optional<Store> oStore = storeService.findByMemberId(rq.getMember().getId());
		if (oStore.isEmpty()) {
			return rq.redirectWithErrorMsg(DEFAULT_ERROR_MESSAGE, "/");
		}

		model.addAttribute("rooms", storeChatService.getStoreChatRooms(oStore.get().getId()));

		return "usr/store/chat/list";
	}

	@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_STORE')")
	@GetMapping("/{roomId}")
	public String getMessage(Model model, @PathVariable Long roomId) {
		final Optional<Store> oStore = storeService.findByMemberId(rq.getMember().getId());
		final Optional<StoreChatRoom> oRoom = storeChatService.findRoomById(roomId);
		if (oStore.isEmpty() || oRoom.isEmpty()) {
			return rq.redirectWithErrorMsg(DEFAULT_ERROR_MESSAGE, "/");
		}

		if (!oRoom.get().getStore().getId().equals(oStore.get().getId())) {
			return rq.historyBack("잘못된 접근입니다.");
		}

		// TODO: chatNotice w chats DTO
		model.addAttribute("roomNotice", StoreChatNoticeDTO.storeOf(oRoom.get()));
		model.addAttribute("chats", storeChatService.getAllMessages(oStore.get().getId()));

		// TODO: offset

		return "usr/store/chat/room";
	}

	@ResponseBody
	@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_STORE')")
	@PostMapping("/{roomId}")
	public List<StoreChatMessageDTO> chatRoom(@NotBlank String content, @PathVariable Long roomId) {
		final Optional<Store> oStore = storeService.findByMemberId(rq.getMember().getId());
		final Optional<StoreChatRoom> oRoom = storeChatService.findRoomById(roomId);
		if (oStore.isEmpty() || oRoom.isEmpty() || !oRoom.get().getStore().getId().equals(oStore.get().getId())) {
			return Collections.emptyList();
		}

		storeChatService.createMessage(oRoom.get(), content, LocalDateTime.now(), rq.getMember());

		// TODO: offset

		return storeChatService.getNewMessages(roomId, oRoom.get().getStoreChatOffset());
	}
}
