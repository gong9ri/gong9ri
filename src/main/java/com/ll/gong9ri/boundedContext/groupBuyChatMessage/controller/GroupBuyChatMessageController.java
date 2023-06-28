package com.ll.gong9ri.boundedContext.groupBuyChatMessage.controller;

import java.security.Principal;
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

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.boundedContext.groupBuyChatMessage.entity.GroupBuyChatMessage;
import com.ll.gong9ri.boundedContext.groupBuyChatMessage.service.GroupBuyChatMessageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GroupBuyChatMessageController {

	private final GroupBuyChatMessageService groupBuyChatMessageService;
	private final Rq rq;

	@MessageMapping("/chats/{roomId}")
	public GroupBuyChatMessage send(@DestinationVariable String roomId, @RequestBody Map<String,String> message, @Headers MessageHeaders headers, Principal principal){

		String content = message.get("content");

		// TODO: 이름말고 member정보 가져오게 변경하기
		String name = principal.getName();

		GroupBuyChatMessage chatMessage = groupBuyChatMessageService.sendChat(content, roomId, name);

		return chatMessage;
	}

	@GetMapping("/{roomId}/messages")
	@ResponseBody
	public List<GroupBuyChatMessage> findAll(@PathVariable Long roomId) {

		// TODO: Offset 구현해서 필요한 메시지만 가져오기.

		return groupBuyChatMessageService.getChatMessagesByRoomId(roomId);
	}
}
