package com.ll.gong9ri.boundedContext.fcm.token;

import com.ll.gong9ri.base.baseEntity.BaseEntity;
import com.ll.gong9ri.boundedContext.member.entity.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Token extends BaseEntity {

	private String tokenString;

	@OneToOne(fetch = FetchType.LAZY)
	@ToString.Exclude
	private Member member;
}
