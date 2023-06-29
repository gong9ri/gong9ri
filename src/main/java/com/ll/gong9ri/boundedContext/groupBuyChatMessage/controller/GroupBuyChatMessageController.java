package com.ll.gong9ri.boundedContext.groupBuyChatMessage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.entity.ChatRoomParticipant;
import com.ll.gong9ri.boundedContext.groupBuyChatMessage.entity.GroupBuyChatMessage;
import com.ll.gong9ri.boundedContext.groupBuyChatMessage.service.GroupBuyChatMessageService;
import com.ll.gong9ri.boundedContext.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GroupBuyChatMessageController {

	private final MemberService memberService;
	private final GroupBuyChatMessageService groupBuyChatMessageService;
	private final Rq rq;

	@MessageMapping("/chats/{roomId}")
	public GroupBuyChatMessage send(@DestinationVariable String roomId, @RequestBody Map<String, String> message) {

		String content = message.get("content");

		String username = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		Long id = memberService.findByUsername(username).get().getId();

		GroupBuyChatMessage chatMessage = groupBuyChatMessageService.sendChat(content, roomId, String.valueOf(id),
			username);

		return chatMessage;
	}

	@GetMapping("/{roomId}/messages")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> findMessages(@PathVariable Long roomId,
		@RequestParam(defaultValue = "no") String isNew) {

		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("memberId", rq.getMember().getId());

		List<GroupBuyChatMessage> chatMessages;

		// 기존 메시지 가져오기
		if (Objects.equals(isNew, "no")) {
			chatMessages = groupBuyChatMessageService.getChatMessagesByRoomId(roomId);
			responseBody.put("messages", chatMessages);
			return ResponseEntity.ok(responseBody);
		}

		// 새로운 메시지 가져오기
		ChatRoomParticipant chatRoomParticipant = rq.getMember().getChatRoomParticipants().stream()
			.filter(p -> p.getGroupBuyChatRoom().getId().equals(roomId))
			.findFirst().orElseThrow(IllegalStateException::new);

		chatMessages = groupBuyChatMessageService.getNewChatMessagesByRoomId(String.valueOf(roomId),
			chatRoomParticipant.getId(),
			chatRoomParticipant.getChatOffset());

		responseBody.put("messages", chatMessages);
		return ResponseEntity.ok(responseBody);
	}
}
