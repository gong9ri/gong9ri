package com.ll.gong9ri.boundedContext.groupBuyChatRoom.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.service.ChatRoomParticipantService;
import com.ll.gong9ri.boundedContext.fcm.dto.TokenDTO;
import com.ll.gong9ri.boundedContext.fcm.service.FcmService;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;
import com.ll.gong9ri.boundedContext.groupBuy.service.GroupBuyMemberService;
import com.ll.gong9ri.boundedContext.groupBuy.service.GroupBuyService;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.dto.GroupBuyChatRoomDto;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.entity.GroupBuyChatRoom;
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
class GroupBuyChatRoomServiceTest {

	@Autowired
	private GroupBuyChatRoomService groupBuyChatRoomService;
	@Autowired
	private GroupBuyService groupBuyService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ProductService productService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private GroupBuyMemberService groupBuyMemberService;
	@Autowired
	private ChatRoomParticipantService chatRoomParticipantService;
	@Autowired
	private FcmService fcmService;

	@Test
	@DisplayName("createChatRoom")
	void createChatRoom() throws Exception {
		// when
		String roomName = "testGroupBuyToCreateChatRoom";
		GroupBuy groupBuy = new GroupBuy().toBuilder().name(roomName).build();
		GroupBuyChatRoom chatRoom = groupBuyChatRoomService.createChatRoom(groupBuy);

		// then
		assertThat(chatRoom.getName()).isEqualTo(roomName);
	}

	@Test
	@DisplayName("Create ChatRoom after groupBuy created")
	void createChatRoomAfterGroupBuyCreated() {
		// given
		RsData<Member> testUserCAGC = memberService.join("testUserCAGC", "1234");
		RsData<Store> testStoreA = storeService.create(testUserCAGC.getData(), "testStoreA");
		RsData<Product> productRsData = productService.registerProduct(testStoreA.getData(),
			ProductRegisterDTO.builder()
				.name("nameCC1")
				.price(10000)
				.description("descCC1")
				.maxPurchaseNum(10)
				.build());

		// when
		RsData<GroupBuy> groupBuyRsData = groupBuyService.create(productRsData.getData());

		// then
		Optional<GroupBuyChatRoom> byId = groupBuyChatRoomService.findByGroupBuyId(groupBuyRsData.getData().getId());
		assertThat(byId.orElseThrow().getName()).isEqualTo(groupBuyRsData.getData().getName());
	}

	@Test
	@DisplayName("find all ChatRooms that member joined in my page")
	void findAllByMemberId() {
		// given
		RsData<Member> testUserFACBM1 = memberService.join("testUserFACBM1", "1234");
		RsData<Store> testStoreFACBM1 = storeService.create(testUserFACBM1.getData(), "testStoreFACBM1");
		RsData<Product> productRsData1 = productService.registerProduct(testStoreFACBM1.getData(),
			ProductRegisterDTO.builder().name("nameF1").price(10000).description("desc1").maxPurchaseNum(10).build());
		RsData<Product> productRsData2 = productService.registerProduct(testStoreFACBM1.getData(),
			ProductRegisterDTO.builder().name("nameF2").price(10000).description("desc1").maxPurchaseNum(10).build());
		RsData<Product> productRsData3 = productService.registerProduct(testStoreFACBM1.getData(),
			ProductRegisterDTO.builder().name("nameF3").price(10000).description("desc1").maxPurchaseNum(10).build());
		RsData<Product> productRsData4 = productService.registerProduct(testStoreFACBM1.getData(),
			ProductRegisterDTO.builder().name("nameF4").price(10000).description("desc1").maxPurchaseNum(10).build());

		GroupBuy groupBuy1 = groupBuyService.create(productRsData1.getData()).getData();
		GroupBuy groupBuy2 = groupBuyService.create(productRsData2.getData()).getData();
		GroupBuy groupBuy3 = groupBuyService.create(productRsData3.getData()).getData();
		GroupBuy groupBuy4 = groupBuyService.create(productRsData4.getData()).getData();

		Member testUserFACBM2 = memberService.join("testUserFACBM2", "1234").getData();
		fcmService.saveToken(new TokenDTO("member2tokenString"), testUserFACBM2);
		groupBuyMemberService.addGeneral(testUserFACBM2, groupBuy1);
		chatRoomParticipantService.createNewParticipant(
			groupBuyChatRoomService.findByGroupBuyId(groupBuy1.getId()).orElseThrow(), testUserFACBM2);
		groupBuyMemberService.addGeneral(testUserFACBM2, groupBuy2);
		chatRoomParticipantService.createNewParticipant(
			groupBuyChatRoomService.findByGroupBuyId(groupBuy2.getId()).orElseThrow(), testUserFACBM2);
		groupBuyMemberService.addGeneral(testUserFACBM2, groupBuy3);
		chatRoomParticipantService.createNewParticipant(
			groupBuyChatRoomService.findByGroupBuyId(groupBuy3.getId()).orElseThrow(), testUserFACBM2);

		Member testUserFACBM3 = memberService.join("testUserFACBM3", "1234").getData();
		fcmService.saveToken(new TokenDTO("member3tokenString"), testUserFACBM3);
		groupBuyMemberService.addGeneral(testUserFACBM3, groupBuy2);
		chatRoomParticipantService.createNewParticipant(
			groupBuyChatRoomService.findByGroupBuyId(groupBuy2.getId()).orElseThrow(), testUserFACBM3);
		groupBuyMemberService.addGeneral(testUserFACBM3, groupBuy3);
		chatRoomParticipantService.createNewParticipant(
			groupBuyChatRoomService.findByGroupBuyId(groupBuy3.getId()).orElseThrow(), testUserFACBM3);
		groupBuyMemberService.addGeneral(testUserFACBM3, groupBuy4);
		chatRoomParticipantService.createNewParticipant(
			groupBuyChatRoomService.findByGroupBuyId(groupBuy4.getId()).orElseThrow(), testUserFACBM3);

		// when
		List<GroupBuyChatRoomDto> user2ChatRoomDtos = groupBuyChatRoomService.findAllByMemberId(testUserFACBM2.getId());
		List<GroupBuyChatRoomDto> user3ChatRoomDtos = groupBuyChatRoomService.findAllByMemberId(testUserFACBM3.getId());

		// then
		assertThat(user2ChatRoomDtos).hasSize(3);
		assertThat(user3ChatRoomDtos).hasSize(3);
		assertThat(user2ChatRoomDtos.get(0).getName()).isEqualTo(groupBuy1.getName());
		assertThat(user2ChatRoomDtos.get(1).getName()).isEqualTo(groupBuy2.getName());
		assertThat(user2ChatRoomDtos.get(2).getName()).isEqualTo(groupBuy3.getName());
	}
}