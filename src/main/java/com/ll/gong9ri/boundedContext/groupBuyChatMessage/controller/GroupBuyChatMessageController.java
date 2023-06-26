package com.ll.gong9ri.boundedContext.groupBuyChatMessage.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ll.gong9ri.boundedContext.groupBuyChatMessage.service.GroupBuyChatMessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GroupBuyChatMessageController {

	private final GroupBuyChatMessageService groupBuyChatMessageService;
}
