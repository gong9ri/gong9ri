package com.ll.gong9ri.boundedContext.chatRoomParticipants.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.boundedContext.chatRoomParticipants.entity.ChatRoomParticipant;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.repository.ChatRoomParticipantRepository;
import com.ll.gong9ri.boundedContext.fcm.service.FcmService;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.entity.GroupBuyChatRoom;
import com.ll.gong9ri.boundedContext.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomParticipantService {

	private final ChatRoomParticipantRepository chatRoomParticipantRepository;
	private final FcmService fcmService;

	@Transactional
	public ChatRoomParticipant createNewParticipant(GroupBuyChatRoom groupBuyChatRoom, Member member) {

		ChatRoomParticipant chatRoomParticipant = ChatRoomParticipant.builder()
			.groupBuyChatRoom(groupBuyChatRoom)
			.member(member)
			.chatOffset("000000000000000000000000")
			.token(fcmService.findByMemberId(member.getId()).getTokenString())
			.build();

		return chatRoomParticipantRepository.save(chatRoomParticipant);
	}

	@Transactional
	public void updateOffset(ChatRoomParticipant chatRoomParticipant, String newOffset) {

		chatRoomParticipantRepository.save(chatRoomParticipant.toBuilder().chatOffset(newOffset).build());
	}

	public Optional<ChatRoomParticipant> findById(Long id) {
		return chatRoomParticipantRepository.findById(id);
	}

	public List<ChatRoomParticipant> findAllByMemberId(Long id) {
		return chatRoomParticipantRepository.findAllByMemberId(id);
	}

	public Optional<ChatRoomParticipant> findByMemberIdAndGroupBuyChatRoomId(Long memberId, Long groupBuyChatRoomId) {
		return chatRoomParticipantRepository.findByMemberIdAndGroupBuyChatRoomId(memberId, groupBuyChatRoomId);
	}

	public List<String> getTokensByChatRoomId(Long roomId) {
		List<ChatRoomParticipant> participants = chatRoomParticipantRepository.findByGroupBuyChatRoomId(roomId);

		return participants.stream()
			.map(ChatRoomParticipant::getToken)
			.toList();
	}
}
