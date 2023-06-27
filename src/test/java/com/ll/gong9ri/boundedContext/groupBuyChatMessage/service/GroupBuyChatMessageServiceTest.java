package com.ll.gong9ri.boundedContext.groupBuyChatMessage.service;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.entity.ChatRoomParticipants;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.service.ChatRoomParticipantsService;
import com.ll.gong9ri.boundedContext.groupBuyChatMessage.entity.GroupBuyChatMessage;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.entity.GroupBuyChatRoom;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.service.GroupBuyChatRoomService;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.service.MemberService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class GroupBuyChatMessageServiceTest {

	@Autowired
	private GroupBuyChatMessageService groupBuyChatMessageService;
	@Autowired
	private ChatRoomParticipantsService chatRoomParticipantsService;
	@Autowired
	private GroupBuyChatRoomService groupBuyChatRoomService;
	@Autowired
	private MemberService memberService;

	@Test
	@DisplayName("샘플메시지 테스트")
	void sampleMessageInsert() throws Exception {
		// when
		String id = groupBuyChatMessageService.sendChat("샘플메시지", "1").getId();

		// then
		Optional<GroupBuyChatMessage> message = groupBuyChatMessageService.findById(id);

		Assertions.assertThat(message.orElseThrow().getContent()).isEqualTo("샘플메시지");
	}

	@Test
	@DisplayName("getNewChatMessages")
	void getNewChatMessages() throws Exception {

		// given
		final String username = "testUser1";
		RsData<Member> rsMember = memberService.join(username, username + username);
		GroupBuyChatRoom groupBuyChatRoom = groupBuyChatRoomService.createChatRoom();
		ChatRoomParticipants participant = chatRoomParticipantsService.createNewParticipant(groupBuyChatRoom,
			rsMember.getData());

		final String roomId = String.valueOf(groupBuyChatRoom.getId());
		final Long participantId = participant.getId();

		groupBuyChatMessageService.sendChat("기존메시지1", roomId);
		groupBuyChatMessageService.getNewChatMessagesByRoomId(roomId, participantId, participant.getChatOffset());

		final String offset = participant.getChatOffset();
		System.out.println(offset);

		// when
		String id1 = groupBuyChatMessageService.sendChat("새 메시지1", roomId).getId();
		String id2 = groupBuyChatMessageService.sendChat("새 메시지2", roomId).getId();
		String id3 = groupBuyChatMessageService.sendChat("새 메시지3", roomId).getId();
		List<GroupBuyChatMessage> messageList = groupBuyChatMessageService.getNewChatMessagesByRoomId(roomId,
			participantId, offset);

		// then
		assertThat(messageList.get(0).getId()).isEqualTo(id1);
		assertThat(messageList.get(1).getId()).isEqualTo(id2);
		assertThat(messageList.get(2).getId()).isEqualTo(id3);

		assertThat(participant.getChatOffset()).isEqualTo(id3);
	}
}