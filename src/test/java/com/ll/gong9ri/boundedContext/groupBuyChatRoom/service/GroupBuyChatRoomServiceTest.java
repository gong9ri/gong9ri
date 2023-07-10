package com.ll.gong9ri.boundedContext.groupBuyChatRoom.service;

import static org.assertj.core.api.Assertions.*;

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
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;
import com.ll.gong9ri.boundedContext.groupBuy.service.GroupBuyService;
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
			new ProductRegisterDTO("sampleProduct1", 10000, "sampleProduct1Description", 30));

		// when
		RsData<GroupBuy> groupBuyRsData = groupBuyService.create(productRsData.getData());

		// then
		Optional<GroupBuyChatRoom> byId = groupBuyChatRoomService.findByGroupBuyId(groupBuyRsData.getData().getId());
		assertThat(byId.orElseThrow().getName()).isEqualTo(groupBuyRsData.getData().getName());
	}
}