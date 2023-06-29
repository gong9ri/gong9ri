package com.ll.gong9ri.boundedContext.storeChat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import com.ll.gong9ri.boundedContext.storeChat.dto.StoreChatMessageDTO;
import com.ll.gong9ri.boundedContext.storeChat.dto.StoreChatNoticeDTO;
import com.ll.gong9ri.boundedContext.storeChat.entity.StoreChatMessage;
import com.ll.gong9ri.boundedContext.storeChat.entity.StoreChatRoom;
import com.ll.gong9ri.boundedContext.storeChat.repository.StoreChatMessageRepository;
import com.ll.gong9ri.boundedContext.storeChat.repository.StoreChatMessageRepositoryImpl;
import com.ll.gong9ri.boundedContext.storeChat.repository.StoreChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreChatService {
	private static final String NOT_ALLOWED_ACCESS_MESSAGE = "잘못된 접근입니다.";
	private final StoreChatRoomRepository roomRepository;
	private final StoreChatMessageRepository messageRepository;
	private final StoreChatMessageRepositoryImpl messageRepositoryImpl;

	@Transactional(readOnly = true)
	public Optional<StoreChatRoom> findRoomById(final Long roomId) {
		return roomRepository.findById(roomId);
	}

	@Transactional(readOnly = true)
	public Optional<StoreChatRoom> findRoomByMemberAndStore(final Member member, final Store store) {
		return roomRepository.findByMemberIdAndStoreId(member.getId(), store.getId());
	}

	private StoreChatRoom createRoom(final Member member, final Store store) {
		StoreChatRoom storeChatRoom = StoreChatRoom.builder()
			.member(member)
			.store(store)
			.build();

		roomRepository.save(storeChatRoom);

		return storeChatRoom;
	}

	public RsData<StoreChatRoom> getRoom(final Member member, final Store store) {
		if (member.getId().equals(store.getMember().getId())) {
			return RsData.of("F-1", NOT_ALLOWED_ACCESS_MESSAGE, null);
		}

		return RsData.successOf(findRoomByMemberAndStore(member, store).orElseGet(() -> createRoom(member, store)));
	}

	public RsData<StoreChatMessage> createMessage(
		final StoreChatRoom storeChatRoom,
		final String content,
		final LocalDateTime createDate,
		final Member member
		final Boolean sentByStore
	) {
		StoreChatMessage storeChatMessage = StoreChatMessage.builder()
			.storeChatRoom(storeChatRoom)
			.content(content)
			.createDate(createDate)
			.sentByStore(member.getId().equals(storeChatRoom.getMember().getId()))
			.build();

		messageRepository.save(storeChatMessage);

		return RsData.successOf(storeChatMessage);
	}

	@Transactional(readOnly = true)
	public List<StoreChatMessageDTO> getAllMessages(final Long storeChatRoomId) {
		return messageRepositoryImpl.findAllByRoomId(storeChatRoomId);
	}

	/**
	 * 해당 채팅방의 offset 이후의 Id 를 가진 메시지를 가져옵니다.
	 *
	 * @param storeChatRoomId 소속한 채팅방의 Id
	 * @param offset 마지막으로 읽은 메시지의 Id
	 * @return StoreChatMessages
	 */
	@Transactional(readOnly = true)
	public List<StoreChatMessageDTO> getNewMessages(final Long storeChatRoomId, final Long offset) {
		return messageRepositoryImpl.findAllByRoomIdAndIdGreaterThan(storeChatRoomId, offset);
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
}
