package com.ll.gong9ri.boundedContext.fcm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.fcm.token.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
	Optional<Token> findByMemberId(final Long memberId);
}
