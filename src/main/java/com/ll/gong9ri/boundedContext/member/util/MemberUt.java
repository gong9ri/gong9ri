package com.ll.gong9ri.boundedContext.member.util;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import com.nimbusds.jose.shaded.gson.JsonDeserializationContext;
import com.nimbusds.jose.shaded.gson.JsonDeserializer;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonParseException;
import com.nimbusds.jose.shaded.gson.JsonPrimitive;
import com.nimbusds.jose.shaded.gson.JsonSerializationContext;
import com.nimbusds.jose.shaded.gson.JsonSerializer;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberUt {

	private static final Gson gson = createGson();
	private static Gson createGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
		return gsonBuilder.create();
	}

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

class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Override
	public JsonElement serialize(LocalDateTime dateTime, Type type, JsonSerializationContext context) {
		return new JsonPrimitive(dateTime.format(formatter));
	}

	@Override
	public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws
		JsonParseException {
		String dateString = json.getAsString();
		return LocalDateTime.parse(dateString, formatter);
	}
}