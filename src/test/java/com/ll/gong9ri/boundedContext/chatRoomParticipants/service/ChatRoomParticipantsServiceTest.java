package com.ll.gong9ri.boundedContext.chatRoomParticipants.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.entity.ChatRoomParticipants;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.entity.GroupBuyChatRoom;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.service.GroupBuyChatRoomService;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.service.MemberService;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class ChatRoomParticipantsServiceTest {

	@Autowired
	private ChatRoomParticipantsService chatRoomParticipantsService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private GroupBuyChatRoomService groupBuyChatRoomService;

	// @Test
	// @DisplayName("create new ChatRoomParticipants")
	// void createNewParticipant() {
	// }

	@Test
	@DisplayName("update chatOffset")
	void updateOffset() {
		// given
		final String username = "testUser2";
		RsData<Member> rsMember = memberService.join(username, username + username);
		GroupBuyChatRoom groupBuyChatRoom = groupBuyChatRoomService.createChatRoom();

		// when
		ChatRoomParticipants newParticipant = chatRoomParticipantsService.createNewParticipant(groupBuyChatRoom,
			rsMember.getData());
		final String newOffset = "111111111111111111111111";
		chatRoomParticipantsService.updateOffset(newParticipant, newOffset);

		// then
		assertThat(newParticipant.getChatOffset()).isEqualTo(newOffset);

	}
}