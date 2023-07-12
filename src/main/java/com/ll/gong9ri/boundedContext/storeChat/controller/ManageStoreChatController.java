package com.ll.gong9ri.boundedContext.storeChat.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import com.ll.gong9ri.boundedContext.store.service.StoreService;
import com.ll.gong9ri.boundedContext.storeChat.dto.StoreChatMessageDTO;
import com.ll.gong9ri.boundedContext.storeChat.dto.StoreChatRoomDTO;
import com.ll.gong9ri.boundedContext.storeChat.entity.StoreChatRoom;
import com.ll.gong9ri.boundedContext.storeChat.service.StoreChatMessageService;
import com.ll.gong9ri.boundedContext.storeChat.service.StoreChatRoomService;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Controller
@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_STORE')")
@RequestMapping("/manage/store/chat")
@RequiredArgsConstructor
public class ManageStoreChatController {
	private static final String DEFAULT_ERROR_MESSAGE = "연결된 스토어가 없습니다. 관리자에게 문의하세요.";
	private final StoreService storeService;
	private final StoreChatRoomService roomService;
	private final StoreChatMessageService messageService;
	private final Rq rq;

	@GetMapping("/")
	public String list(Model model) {
		final Optional<Store> oStore = storeService.findByMemberId(rq.getMember().getId());
		if (oStore.isEmpty()) {
			return rq.redirectWithErrorMsg(DEFAULT_ERROR_MESSAGE, "/");
		}

		model.addAttribute("rooms", roomService.getStoreChatRooms(oStore.get().getId()));

		return "usr/store/chat/list";
	}

	@GetMapping("/{roomId}")
	public String room(Model model, @PathVariable Long roomId) {
		final Optional<Store> oStore = storeService.findByMemberId(rq.getMember().getId());
		final Optional<StoreChatRoom> oRoom = roomService.findRoomById(roomId);
		if (oStore.isEmpty() || oRoom.isEmpty()) {
			return rq.redirectWithErrorMsg(DEFAULT_ERROR_MESSAGE, "/");
		}

		RsData<StoreChatRoomDTO> rsRoom = roomService.getMemberRoomDTO(oRoom.get(), oStore.get());
		if (rsRoom.isFail()) {
			return rq.historyBack(rsRoom);
		}

		model.addAttribute("room", rsRoom.getData());

		return "usr/store/chat/room";
	}

	@ResponseBody
	@PostMapping("/{roomId}")
	public ResponseEntity<List<StoreChatMessageDTO>> send(@NotBlank String content, @PathVariable Long roomId) {
		final Optional<Store> oStore = storeService.findByMemberId(rq.getMember().getId());
		final Optional<StoreChatRoom> oRoom = roomService.findRoomById(roomId);
		if (oStore.isEmpty() || oRoom.isEmpty() || !oRoom.get().getStore().getId().equals(oStore.get().getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		messageService.createMessage(oRoom.get(), content, LocalDateTime.now(), rq.getMember());

		// TODO: noti push event

		return ResponseEntity.ok(messageService.getNewMessages(roomId, oRoom.get().getStoreChatOffset()));
	}
}
