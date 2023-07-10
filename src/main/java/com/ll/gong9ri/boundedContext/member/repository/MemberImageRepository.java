package com.ll.gong9ri.boundedContext.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.gong9ri.boundedContext.member.entity.MemberImage;

public interface MemberImageRepository extends JpaRepository<MemberImage, Long>{
	Optional<MemberImage> findByMemberId(Long member);
}
