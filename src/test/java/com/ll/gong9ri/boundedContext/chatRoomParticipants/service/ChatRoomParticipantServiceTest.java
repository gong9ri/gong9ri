package com.ll.gong9ri.boundedContext.chatRoomParticipants.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.entity.ChatRoomParticipant;
import com.ll.gong9ri.boundedContext.fcm.dto.TokenDTO;
import com.ll.gong9ri.boundedContext.fcm.service.FcmService;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.entity.GroupBuyChatRoom;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.service.GroupBuyChatRoomService;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.service.MemberService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class ChatRoomParticipantServiceTest {

	@Autowired
	private ChatRoomParticipantService chatRoomParticipantService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private GroupBuyChatRoomService groupBuyChatRoomService;
	@Autowired
	private FcmService fcmService;

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
		GroupBuy testGroupBuy1 = new GroupBuy().toBuilder().name("testGroupBuy1").build();
		GroupBuyChatRoom groupBuyChatRoom = groupBuyChatRoomService.createChatRoom(testGroupBuy1);
		fcmService.saveToken(new TokenDTO("newTestToken2"), rsMember.getData());

		// when
		ChatRoomParticipant newParticipant = chatRoomParticipantService.createNewParticipant(groupBuyChatRoom,
			rsMember.getData());
		final String newOffset = "111111111111111111111111";
		chatRoomParticipantService.updateOffset(newParticipant, newOffset);

		// then
		assertThat(newParticipant.getChatOffset()).isEqualTo(newOffset);

	}
}