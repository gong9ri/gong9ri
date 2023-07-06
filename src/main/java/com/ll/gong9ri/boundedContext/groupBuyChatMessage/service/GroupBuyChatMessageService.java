package com.ll.gong9ri.boundedContext.groupBuyChatMessage.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.event.NewChatReceivedEvent;
import com.ll.gong9ri.boundedContext.groupBuyChatMessage.entity.GroupBuyChatMessage;
import com.ll.gong9ri.boundedContext.groupBuyChatMessage.repository.GroupBuyChatMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupBuyChatMessageService {

	private final SimpMessagingTemplate messagingTemplate;
	private final GroupBuyChatMessageRepository groupBuyChatMessageRepository;
	private final ApplicationEventPublisher publisher;

	public GroupBuyChatMessage sendChat(String content, String chatType, String roomId, String senderId,
		String senderName) {
		GroupBuyChatMessage groupBuyChatMessage = GroupBuyChatMessage.builder()
			.content(content)
			.chatType(chatType)
			.senderId(senderId)
			.senderName(senderName)
			.chatRoomId(roomId)
			.createDate(LocalDateTime.now())
			.build();

		messagingTemplate.convertAndSend("/topic/" + roomId, groupBuyChatMessage);

		return groupBuyChatMessageRepository.save(groupBuyChatMessage);
	}
	public GroupBuyChatMessage sendChat(String content, String roomId) {
		GroupBuyChatMessage groupBuyChatMessage = GroupBuyChatMessage.builder()
			.content(content)
			.chatRoomId(roomId)
			.createDate(LocalDateTime.now())
			.build();

		messagingTemplate.convertAndSend("/topic/" + roomId, groupBuyChatMessage);

		return groupBuyChatMessageRepository.save(groupBuyChatMessage);
	}

	public Optional<GroupBuyChatMessage> findById(String id) {
		return groupBuyChatMessageRepository.findById(id);
	}

	public List<GroupBuyChatMessage> getChatMessagesByRoomId(Long roomId) {

		return groupBuyChatMessageRepository.findByChatRoomId(String.valueOf(roomId));
	}

	/**
	 * 현재 offset보다 최신의 메시지를 전달해줌.
	 * 전달받은 가장 최근 새 메시지 id로 offset을 변경한다.
	 * @param roomId String
	 * @param offset String
	 * @return List<GroupBuyChatMessage>
	 */
	public List<GroupBuyChatMessage> getNewChatMessagesByRoomId(String roomId, Long participantId, String offset) {

		ObjectId lastObjectId = offset != null ? new ObjectId(offset) : new ObjectId("000000000000000000000000");

		List<GroupBuyChatMessage> chatMessages = groupBuyChatMessageRepository.findNewChatMessages(roomId,
			lastObjectId);

		if (!chatMessages.isEmpty()) {
			String newOffset = chatMessages.get(chatMessages.size() - 1).getId();
			publisher.publishEvent(new NewChatReceivedEvent(participantId, newOffset));
		}

		return chatMessages;
	}
}
