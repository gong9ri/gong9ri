package com.ll.gong9ri.boundedContext.groupBuyChatMessage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rq.WsRq;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.entity.ChatRoomParticipant;
import com.ll.gong9ri.boundedContext.groupBuyChatMessage.entity.GroupBuyChatMessage;
import com.ll.gong9ri.boundedContext.groupBuyChatMessage.service.GroupBuyChatMessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GroupBuyChatMessageController {
	private final GroupBuyChatMessageService groupBuyChatMessageService;
	private final Rq rq;
	private final WsRq wsRq;

	@MessageMapping("/chats/{roomId}")
	public GroupBuyChatMessage send(@DestinationVariable String roomId, @RequestBody Map<String, String> message) {
		GroupBuyChatMessage chatMessage = groupBuyChatMessageService.sendChat(
			message.get("content"),
			roomId, String.valueOf(wsRq.getMember().getId())
		);

		return chatMessage;
	}

	@GetMapping("/{roomId}/messages")
	@ResponseBody
	public List<GroupBuyChatMessage> findMessages(@PathVariable Long roomId,
		@RequestParam(defaultValue = "1") Long fromId) {

		if (fromId == 1L) {
			return groupBuyChatMessageService.getChatMessagesByRoomId(roomId);
		}

		ChatRoomParticipant chatRoomParticipant = rq.getMember().getChatRoomParticipants().stream()
			.filter(p -> p.getGroupBuyChatRoom().getId().equals(roomId))
			.findFirst().orElseThrow();

		return groupBuyChatMessageService.getNewChatMessagesByRoomId(String.valueOf(roomId),
			chatRoomParticipant.getId(),
			chatRoomParticipant.getChatOffset());
	}
}
