package com.ll.gong9ri.boundedContext.storeChat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import com.ll.gong9ri.boundedContext.storeChat.dto.StoreChatMessageDTO;
import com.ll.gong9ri.boundedContext.storeChat.dto.StoreChatNoticeDTO;
import com.ll.gong9ri.boundedContext.storeChat.dto.StoreChatRoomDTO;
import com.ll.gong9ri.boundedContext.storeChat.entity.StoreChatRoom;
import com.ll.gong9ri.boundedContext.storeChat.repository.StoreChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreChatRoomService {
	private static final String NOT_ALLOWED_ACCESS_MESSAGE = "잘못된 접근입니다.";
	private final StoreChatRoomRepository roomRepository;
	private final StoreChatMessageService messageService;

	@Transactional(readOnly = true)
	public Optional<StoreChatRoom> findRoomById(final Long roomId) {
		return roomRepository.findById(roomId);
	}

	@Transactional(readOnly = true)
	public Optional<StoreChatRoom> findRoomByMemberAndStore(final Member member, final Store store) {
		return roomRepository.findByMemberIdAndStoreId(member.getId(), store.getId());
	}

	public StoreChatRoom createRoom(final Member member, final Store store) {
		StoreChatRoom storeChatRoom = StoreChatRoom.builder()
			.member(member)
			.store(store)
			.build();

		roomRepository.save(storeChatRoom);

		return storeChatRoom;
	}

	public RsData<StoreChatRoom> getMemberRoom(final Member member, final Store store) {
		if (member.getId().equals(store.getMember().getId())) {
			return RsData.of("F-1", NOT_ALLOWED_ACCESS_MESSAGE, null);
		}

		return RsData.successOf(findRoomByMemberAndStore(member, store)
			.orElseGet(() -> createRoom(member, store)));
	}

	public void setOffset(StoreChatRoom room, final Long offset, final Boolean isMember) {
		room = Boolean.TRUE.equals(isMember)
			? room.toBuilder().memberChatOffset(offset).build()
			: room.toBuilder().storeChatOffset(offset).build();

		roomRepository.save(room);
	}

	@Transactional(readOnly = true)
	public List<StoreChatNoticeDTO> getMemberChatRooms(final Long memberId) {
		return roomRepository.findAllByMemberId(memberId).stream()
			.map(e -> StoreChatNoticeDTO.builder()
				.roomId(e.getId())
				.senderName(e.getStore().getName())
				.chatOffset(e.getMemberChatOffset())
				.noticeCount(e.getMemberNoticeCount())
				.build())
			.toList();
	}

	@Transactional(readOnly = true)
	public List<StoreChatNoticeDTO> getStoreChatRooms(final Long storeId) {
		return roomRepository.findAllByStoreId(storeId).stream()
			.map(e -> StoreChatNoticeDTO.builder()
				.roomId(e.getId())
				.senderName(e.getMember().getUsername())
				.chatOffset(e.getStoreChatOffset())
				.noticeCount(e.getStoreNoticeCount())
				.build())
			.toList();
	}

	public RsData<StoreChatRoomDTO> getMemberRoomDTO(StoreChatRoom room, final Store store) {
		if (!room.getStore().getId().equals(store.getId())) {
			return RsData.failOf(null);
		}

		final List<StoreChatMessageDTO> messages = messageService.getAllMessages(room.getId());
		setOffset(room, messages.stream().mapToLong(StoreChatMessageDTO::getId).max().orElse(Long.MIN_VALUE), false);

		return RsData.successOf(StoreChatRoomDTO.builder()
			.room(StoreChatNoticeDTO.storeOf(room))
			.messages(messages)
			.build()
		);
	}

	public RsData<StoreChatRoomDTO> getStoreRoomDTO(StoreChatRoom room, final Member member) {
		if (!room.getMember().getId().equals(member.getId())) {
			return RsData.failOf(null);
		}

		final List<StoreChatMessageDTO> messages = messageService.getAllMessages(room.getId());
		setOffset(room, messages.stream().mapToLong(StoreChatMessageDTO::getId).max().orElse(Long.MIN_VALUE), true);

		return RsData.successOf(StoreChatRoomDTO.builder()
			.room(StoreChatNoticeDTO.memberOf(room))
			.messages(messages)
			.build()
		);
	}
}
