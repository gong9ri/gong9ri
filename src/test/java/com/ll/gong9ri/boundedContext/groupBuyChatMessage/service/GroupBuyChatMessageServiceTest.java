package com.ll.gong9ri.boundedContext.groupBuyChatMessage.service;

import static org.assertj.core.api.Assertions.*;

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

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.entity.ChatRoomParticipant;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.service.ChatRoomParticipantService;
import com.ll.gong9ri.boundedContext.fcm.dto.TokenDTO;
import com.ll.gong9ri.boundedContext.fcm.service.FcmService;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;
import com.ll.gong9ri.boundedContext.groupBuy.service.GroupBuyService;
import com.ll.gong9ri.boundedContext.groupBuyChatMessage.entity.GroupBuyChatMessage;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.entity.GroupBuyChatRoom;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.service.GroupBuyChatRoomService;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.service.MemberService;
import com.ll.gong9ri.boundedContext.product.dto.ProductRegisterDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.service.ProductService;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import com.ll.gong9ri.boundedContext.store.service.StoreService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class GroupBuyChatMessageServiceTest {

	@Autowired
	private GroupBuyChatMessageService groupBuyChatMessageService;
	@Autowired
	private ChatRoomParticipantService chatRoomParticipantService;
	@Autowired
	private GroupBuyChatRoomService groupBuyChatRoomService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private FcmService fcmService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private ProductService productService;
	@Autowired
	private GroupBuyService groupBuyService;

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
		RsData<Store> testStoreGNCM1 = storeService.create(rsMember.getData(), "testStoreGNCM1");
		RsData<Product> productRsData1 = productService.registerProduct(testStoreGNCM1.getData(),
			new ProductRegisterDTO("sampleProduct1", 10000, "sampleProduct1Description1", 30));
		RsData<GroupBuy> groupBuyRsData = groupBuyService.create(productRsData1.getData());
		GroupBuyChatRoom groupBuyChatRoom = groupBuyChatRoomService.createChatRoom(groupBuyRsData.getData());
		fcmService.saveToken(new TokenDTO("newTestToken1"), rsMember.getData());
		ChatRoomParticipant participant = chatRoomParticipantService.createNewParticipant(groupBuyChatRoom,
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