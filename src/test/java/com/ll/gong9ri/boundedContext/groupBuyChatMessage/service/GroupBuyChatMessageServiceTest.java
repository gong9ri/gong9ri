package com.ll.gong9ri.boundedContext.groupBuyChatMessage.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ll.gong9ri.boundedContext.groupBuyChatMessage.entity.GroupBuyChatMessage;

@SpringBootTest
class GroupBuyChatMessageServiceTest {

	@Autowired
	private GroupBuyChatMessageService groupBuyChatMessageService;

	@Test
	@DisplayName("샘플메시지 테스트")
	void sampleMessageInsert() throws Exception{
		//when
	    String id = groupBuyChatMessageService.sendChat("샘플메시지", "1").getId();

	    //then
		Optional<GroupBuyChatMessage> message = groupBuyChatMessageService.findById(id);

		Assertions.assertThat(message.orElseThrow().getContent()).isEqualTo("샘플메시지");
	}
}