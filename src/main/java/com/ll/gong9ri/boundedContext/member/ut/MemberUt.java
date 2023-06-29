package com.ll.gong9ri.boundedContext.member.ut;

import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberUt {
	public static Member deserialize(String json) {
		Gson gson = new Gson();

		return gson.fromJson(json, new TypeToken<Member>() {
		}.getType());
	}
}
