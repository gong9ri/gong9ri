package com.ll.gong9ri.boundedContext.chatRoomParticipants.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ll.gong9ri.boundedContext.chatRoomParticipants.entity.ChatRoomParticipant;

@Repository
public interface ChatRoomParticipantRepository extends JpaRepository<ChatRoomParticipant, Long> {
	List<ChatRoomParticipant> findAllByMemberId(final Long memberId);

	Optional<ChatRoomParticipant> findByMemberIdAndGroupBuyChatRoomId(final Long memberId, final Long roomId);
}
