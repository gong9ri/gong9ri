package com.ll.gong9ri.boundedContext.groupBuyChatRoom.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GroupBuyChatRoomServiceTest {

	@Autowired
	private GroupBuyChatRoomService groupBuyChatRoomService;

	@Test
	@DisplayName("createChatRoom")
	public void createChatRoom() throws Exception{
	    // when
	    groupBuyChatRoomService.createChatRoom();

		// TODO: 생성테스트 작성
	}
}