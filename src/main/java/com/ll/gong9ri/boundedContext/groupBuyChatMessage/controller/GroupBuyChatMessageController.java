package com.ll.gong9ri.boundedContext.groupBuyChatMessage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rq.WsRq;
import com.ll.gong9ri.boundedContext.groupBuyChatMessage.dto.ChatResponse;
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
			message.get("chatType"),
			roomId, String.valueOf(wsRq.getMember().getId()), wsRq.getMember().getUsername()
		);

		return chatMessage;
	}

	/**
	 * 메시지 전체 가져오기
	 * @param roomId ChatRoomId
	 * @return ResponseEntity<ChatResponse>
	 */
	@GetMapping("/{roomId}/messages")
	@ResponseBody
	public ResponseEntity<ChatResponse> findMessages(@PathVariable Long roomId) {

		List<GroupBuyChatMessage> chatMessages = groupBuyChatMessageService.getChatMessagesByRoomId(roomId);

		return ResponseEntity.ok(new ChatResponse(chatMessages, rq.getMember().getId()));
	}
}
