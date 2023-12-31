package com.ll.gong9ri.boundedContext.storeChat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.storeChat.entity.StoreChatRoom;

public interface StoreChatRoomRepository extends JpaRepository<StoreChatRoom, Long> {
	Optional<StoreChatRoom> findByMemberIdAndStoreId(final Long memberId, final Long storeId);

	List<StoreChatRoom> findAllByMemberId(final Long memberId);

	List<StoreChatRoom> findAllByStoreId(final Long storeId);
}
