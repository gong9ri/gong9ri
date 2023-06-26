package com.ll.gong9ri.boundedContext.chatRoomParticipants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ll.gong9ri.boundedContext.chatRoomParticipants.entity.ChatRoomParticipants;

@Repository
public interface ChatRoomParticipantsRepository extends JpaRepository<ChatRoomParticipants, Long> {
}
