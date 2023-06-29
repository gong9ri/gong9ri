package com.ll.gong9ri.boundedContext.member.util;

import java.time.LocalDateTime;
import java.util.Map;

import com.ll.gong9ri.boundedContext.member.entity.AuthLevel;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.entity.ProviderTypeCode;
import com.ll.gong9ri.standard.util.Ut;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberUt {
	public static Map<String, Object> toMap(final Member member) {
		return Ut.mapOf(
			"id", member.getId(),
			"createDate", member.getCreateDate(),
			"modifyDate", member.getModifyDate(),
			"providerTypeCode", member.getProviderTypeCode(),
			"authLevel", member.getAuthLevel(),
			"username", member.getUsername(),
			"nickname", member.getNickname()
		);
	}

	public static Member fromMap(final Map<String, Object> map) {
		Long id = (Long)map.get("id");
		LocalDateTime createDate = (LocalDateTime)map.get("createDate");
		LocalDateTime modifyDate = (LocalDateTime)map.get("modifyDate");
		ProviderTypeCode providerTypeCode = (ProviderTypeCode)map.get("providerTypeCode");
		AuthLevel authLevel = (AuthLevel)map.get("authLevel");
		String username = (String)map.get("username");
		String nickname = (String)map.get("nickname");

		return Member.builder()
			.providerTypeCode(providerTypeCode)
			.authLevel(authLevel)
			.username(username)
			.nickname(nickname)
			.id(id)
			.createDate(createDate)
			.modifyDate(modifyDate)
			.build();
	}
}

