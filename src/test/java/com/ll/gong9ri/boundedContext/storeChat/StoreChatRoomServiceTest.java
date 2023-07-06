package com.ll.gong9ri.boundedContext.storeChat;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.LongStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.baseEntity.BaseEntity;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.service.MemberService;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import com.ll.gong9ri.boundedContext.store.service.StoreService;
import com.ll.gong9ri.boundedContext.storeChat.entity.StoreChatRoom;
import com.ll.gong9ri.boundedContext.storeChat.service.StoreChatMessageService;
import com.ll.gong9ri.boundedContext.storeChat.service.StoreChatRoomService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class StoreChatRoomServiceTest {
	@Autowired
	private MemberService memberService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private StoreChatRoomService roomService;
	@Autowired
	private StoreChatMessageService messageService;

	private Member normalMember, storeMember;
	private Store store;

	@BeforeEach
	void beforeAll() {
		final String memberName = "smdlfkdsklfsdl";
		final String storeName = "poipdmkentd";
		normalMember = memberService.join(memberName, memberName + memberName).getData();
		storeMember = memberService.storeJoin(storeName, storeName + storeName).getData();
		store = storeService.findByMemberId(storeMember.getId()).orElseThrow();
	}

	@Test
	@DisplayName("storeChat room request")
	void chatRoomRequest() {
		final Optional<StoreChatRoom> oRoom = roomService.findRoomByMemberAndStore(storeMember, store);
		assertThat(oRoom).isEmpty();

		final RsData<StoreChatRoom> rsRoom = roomService.getMemberRoom(normalMember, store);
		assertThat(rsRoom.isSuccess()).isTrue();

		assertThat(rsRoom.getData().getId())
			.isEqualTo(roomService.getMemberRoom(normalMember, store).getData().getId());
	}

	@Test
	@DisplayName("chatting test")
	void chatRoomSendAndRead() {
		final RsData<StoreChatRoom> rsRoom = roomService.getMemberRoom(normalMember, store);
		final String content = "hi";
		assertThat(rsRoom.isSuccess()).isTrue();
		final StoreChatRoom room = rsRoom.getData();

		// TODO: last sent message doesnt mean last seen message
		final Long normalUserLastReadOffset = LongStream.range(1, 23)
			.mapToObj(i -> messageService.createMessage(
				room,
				content + i,
				LocalDateTime.now(),
				normalMember
			))
			.filter(RsData::isSuccess)
			.map(RsData::getData)
			.mapToLong(BaseEntity::getId)
			.max()
			.orElseThrow();

		messageService.getAllMessages(room.getId())
			.forEach(message -> assertThat(message.getContent()).contains(content));
		assertThat(messageService.getNewMessages(room.getId(), normalUserLastReadOffset)).isEmpty();

		final Long lastMessageId = LongStream.range(1, 23)
			.map(i -> i + normalUserLastReadOffset)
			.mapToObj(i -> messageService.createMessage(
				room,
				content + i,
				LocalDateTime.now(),
				normalMember
			))
			.filter(RsData::isSuccess)
			.map(RsData::getData)
			.mapToLong(BaseEntity::getId)
			.max()
			.orElseThrow();

		assertThat(messageService.getNewMessages(room.getId(), normalUserLastReadOffset)).isNotEmpty();
		assertThat(normalUserLastReadOffset).isLessThan(lastMessageId);
	}
}
