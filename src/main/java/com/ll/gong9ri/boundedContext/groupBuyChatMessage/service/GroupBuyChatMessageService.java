package com.ll.gong9ri.boundedContext.groupBuyChatMessage.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.boundedContext.chatRoomParticipants.entity.ChatRoomParticipant;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.service.ChatRoomParticipantService;
import com.ll.gong9ri.boundedContext.groupBuyChatMessage.entity.GroupBuyChatMessage;
import com.ll.gong9ri.boundedContext.groupBuyChatMessage.repository.GroupBuyChatMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupBuyChatMessageService {

	private final SimpMessagingTemplate messagingTemplate;
	private final GroupBuyChatMessageRepository groupBuyChatMessageRepository;
	private final ChatRoomParticipantService chatRoomParticipantService;

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
	 * 프론트에 새로운 메시지를 전달해줌.
	 * offset을 전달받은 가장 최근 새 메시지 id로 변경한다.
	 * @param roomId String
	 * @param participantId Long
	 * @param offset String
	 * @return List<GroupBuyChatMessage>
	 */
	public List<GroupBuyChatMessage> getNewChatMessagesByRoomId(String roomId, Long participantId, String offset) {

		Optional<ChatRoomParticipant> participant = chatRoomParticipantService.findById(participantId);

		ObjectId lastObjectId = offset != null ? new ObjectId(offset) : new ObjectId("000000000000000000000000");

		List<GroupBuyChatMessage> chatMessages = groupBuyChatMessageRepository.findNewChatMessages(roomId,
			lastObjectId);

		// TODO: 분리하기
		if (!chatMessages.isEmpty()) {
			String newOffset = chatMessages.get(chatMessages.size() - 1).getId();
			chatRoomParticipantService.updateOffset(participant.orElseThrow(), newOffset);
		}

		return chatMessages;
	}
}
