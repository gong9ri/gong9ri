package com.ll.gong9ri.boundedContext.storeChat.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.event.EventAfterStoreChatMessageCreate;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.storeChat.dto.StoreChatMessageDTO;
import com.ll.gong9ri.boundedContext.storeChat.entity.StoreChatMessage;
import com.ll.gong9ri.boundedContext.storeChat.entity.StoreChatRoom;
import com.ll.gong9ri.boundedContext.storeChat.repository.StoreChatMessageRepository;
import com.ll.gong9ri.boundedContext.storeChat.repository.StoreChatMessageRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreChatMessageService {
	private final StoreChatMessageRepository messageRepository;
	private final StoreChatMessageRepositoryImpl messageRepositoryImpl;
	private final ApplicationEventPublisher publisher;

	public RsData<StoreChatMessage> createMessage(
		final StoreChatRoom storeChatRoom,
		final String content,
		final LocalDateTime createDate,
		final Member member
	) {
		final Boolean isMember = member.getId().equals(storeChatRoom.getMember().getId());
		StoreChatMessage storeChatMessage = StoreChatMessage.builder()
			.storeChatRoom(storeChatRoom)
			.content(content)
			.createDate(createDate)
			.sentByMember(isMember)
			.build();

		messageRepository.save(storeChatMessage);

		publisher.publishEvent(
			new EventAfterStoreChatMessageCreate(storeChatRoom, storeChatMessage.getId(), isMember)
		);

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
}
