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
	private final StoreChatRoomRepository storeChatRoomRepository;
	private final StoreChatMessageRepository storeChatMessageRepository;
	private final StoreChatMessageRepositoryImpl storeChatMessageRepositoryImpl;

	@Transactional(readOnly = true)
	public Optional<StoreChatRoom> findRoomById(final Long roomId) {
		return storeChatRoomRepository.findById(roomId);
	}

	@Transactional(readOnly = true)
	public Optional<StoreChatRoom> findRoomByMemberAndStore(final Member member, final Store store) {
		return storeChatRoomRepository.findByMemberIdAndStoreId(member.getId(), store.getId());
	}

	private StoreChatRoom createRoom(final Member member, final Store store) {
		StoreChatRoom storeChatRoom = StoreChatRoom.builder()
			.member(member)
			.store(store)
			.build();

		storeChatRoomRepository.save(storeChatRoom);

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
		final Boolean sentByStore
	) {
		StoreChatMessage storeChatMessage = StoreChatMessage.builder()
			.storeChatRoom(storeChatRoom)
			.content(content)
			.createDate(createDate)
			.sentByStore(sentByStore)
			.build();

		storeChatMessageRepository.save(storeChatMessage);

		return RsData.successOf(storeChatMessage);
	}

	@Transactional(readOnly = true)
	public List<StoreChatMessageDTO> getAllMessages(final StoreChatRoom storeChatRoom) {
		return storeChatMessageRepositoryImpl.findAllByRoomId(storeChatRoom.getId());
	}

	/**
	 * 해당 채팅방의 offset 이후의 Id 를 가진 메시지를 가져옵니다.
	 *
	 * @param storeChatRoom 소속한 채팅방
	 * @param offset 마지막으로 읽은 메시지의 Id
	 * @return StoreChatMessages
	 */
	@Transactional(readOnly = true)
	public List<StoreChatMessageDTO> getNewMessages(final StoreChatRoom storeChatRoom, final Long offset) {
		return storeChatMessageRepositoryImpl.findAllByRoomIdAndIdGreaterThan(storeChatRoom.getId(), offset);
	}
}
