package com.ll.gong9ri.boundedContext.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.image.entity.ChatImage;

public interface ChatImageRepository extends JpaRepository<ChatImage, Long> {
}
