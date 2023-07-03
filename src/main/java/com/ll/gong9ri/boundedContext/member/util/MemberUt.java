package com.ll.gong9ri.boundedContext.member.util;

import java.util.Map;

import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberUt {
	private static final Gson gson = new GsonBuilder().create();

	private static String serializeMember(Member member) {
		return gson.toJson(member);
	}

	private static Member deserializeMember(String json) {
		return gson.fromJson(json, Member.class);
	}

	public static Map<String, Object> serializeMemberToMap(Member member) {
		String json = serializeMember(member);
		return gson.fromJson(json, new TypeToken<Map<String, Object>>() {
		}.getType());
	}

	public static Member deserializeMemberFromMap(Map<String, Object> map) {
		String json = gson.toJson(map);
		return deserializeMember(json);
	}
}
