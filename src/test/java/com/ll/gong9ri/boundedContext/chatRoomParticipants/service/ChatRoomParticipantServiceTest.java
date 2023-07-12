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
import com.ll.gong9ri.boundedContext.groupBuy.service.GroupBuyService;
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
class ChatRoomParticipantServiceTest {

	@Autowired
	private ChatRoomParticipantService chatRoomParticipantService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private GroupBuyChatRoomService groupBuyChatRoomService;
	@Autowired
	private FcmService fcmService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private ProductService productService;
	@Autowired
	private GroupBuyService groupBuyService;

	@Test
	@DisplayName("update chatOffset")
	void updateOffset() {
		// given
		final String username = "testUser2";
		RsData<Member> rsMember = memberService.join(username, username + username);
		RsData<Store> testStoreUC1 = storeService.create(rsMember.getData(), "testStoreUC1");
		RsData<Product> productRsData1 = productService.registerProduct(testStoreUC1.getData(),
			new ProductRegisterDTO("sampleProduct1", 10000, "sampleProduct1Description1", 30));
		RsData<GroupBuy> groupBuyRsData = groupBuyService.create(productRsData1.getData());
		GroupBuyChatRoom groupBuyChatRoom = groupBuyChatRoomService.createChatRoom(groupBuyRsData.getData());
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