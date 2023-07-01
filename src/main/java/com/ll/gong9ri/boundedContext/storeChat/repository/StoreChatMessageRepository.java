package com.ll.gong9ri.boundedContext.storeChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.storeChat.entity.StoreChatMessage;

public interface StoreChatMessageRepository extends JpaRepository<StoreChatMessage, Long> {
}
