package com.ll.gong9ri.boundedContext.groupBuyChatMessage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ll.gong9ri.boundedContext.groupBuyChatMessage.entity.GroupBuyChatMessage;
import com.ll.gong9ri.boundedContext.groupBuyChatMessage.service.GroupBuyChatMessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GroupBuyChatMessageController {

	private final GroupBuyChatMessageService groupBuyChatMessageService;

	@MessageMapping("/chats/{roomId}")
	public GroupBuyChatMessage send(@DestinationVariable String roomId, @RequestBody Map<String,String> message, @Headers MessageHeaders headers){

		String content = message.get("content");

		GroupBuyChatMessage chatMessage = groupBuyChatMessageService.sendChat(content, roomId);

		return chatMessage;
	}

	@GetMapping("/{roomId}/messages")
	@ResponseBody
	public List<GroupBuyChatMessage> findAll(@PathVariable Long roomId) {

		// TODO: Offset 구현해서 필요한 메시지만 가져오기.

		return groupBuyChatMessageService.getChatMessagesByRoomId(roomId);
	}
}
