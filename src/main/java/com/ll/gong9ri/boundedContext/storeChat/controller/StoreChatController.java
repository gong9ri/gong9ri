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
import com.ll.gong9ri.boundedContext.storeChat.dto.StoreChatMessageDTO;
import com.ll.gong9ri.boundedContext.storeChat.entity.StoreChatRoom;
import com.ll.gong9ri.boundedContext.storeChat.service.StoreChatMessageService;
import com.ll.gong9ri.boundedContext.storeChat.service.StoreChatRoomService;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/store/chat")
@RequiredArgsConstructor
public class StoreChatController {
	private final StoreChatRoomService roomService;
	private final StoreChatMessageService messageService;
	private final Rq rq;

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/{roomId}")
	public String chatRoom(Model model, @PathVariable Long roomId) {
		final Optional<StoreChatRoom> oRoom = roomService.findRoomById(roomId);
		if (oRoom.isEmpty()) {
			return rq.historyBack("잘못된 접근입니다.");
		}

		model.addAttribute("room", roomService.getStoreRoomDTO(oRoom.get(), rq.getMember()));

		return "usr/store/chatroom";
	}

	@ResponseBody
	@PostMapping("/{roomId}")
	public ResponseEntity<List<StoreChatMessageDTO>> send(@NotBlank String content, @PathVariable Long roomId) {
		final Optional<StoreChatRoom> oRoom = roomService.findRoomById(roomId);
		if (oRoom.isEmpty() || !oRoom.get().getMember().getId().equals(rq.getMember().getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		messageService.createMessage(oRoom.get(), content, LocalDateTime.now(), rq.getMember());

		return ResponseEntity.ok(messageService.getNewMessages(roomId, oRoom.get().getMemberChatOffset()));
	}
}
