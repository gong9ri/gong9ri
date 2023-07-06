package com.ll.gong9ri.standard.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.gong9ri.base.appConfig.AppConfig;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Ut {
	private static ObjectMapper getObjectMapper() {
		return (ObjectMapper)AppConfig.getContext().getBean("objectMapper");
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class url {
		public static String encode(String str) {
			return URLEncoder.encode(str, StandardCharsets.UTF_8);
		}

		public static String modifyQueryParam(String url, String paramName, String paramValue) {
			url = deleteQueryParam(url, paramName);
			url = addQueryParam(url, paramName, paramValue);

			return url;
		}

		public static String addQueryParam(String url, String paramName, String paramValue) {
			if (!url.contains("?")) {
				url += "?";
			}

			if (!url.endsWith("?") && !url.endsWith("&")) {
				url += "&";
			}

			url += paramName + "=" + paramValue;

			return url;
		}

		private static String deleteQueryParam(String url, String paramName) {
			int startPoint = url.indexOf(paramName + "=");
			if (startPoint == -1)
				return url;

			int endPoint = url.substring(startPoint).indexOf("&");

			if (endPoint == -1) {
				return url.substring(0, startPoint - 1);
			}

			String urlAfter = url.substring(startPoint + endPoint + 1);

			return url.substring(0, startPoint) + urlAfter;
		}
	}

	public static <K, V> Map<K, V> mapOf(Object... args) {
		Map<K, V> map = new LinkedHashMap<>();
		int size = args.length / 2;
		for (int i = 0; i < size; i++) {
			int keyIndex = i * 2;
			int valueIndex = keyIndex + 1;

			K key = (K)args[keyIndex];
			V value = (V)args[valueIndex];

			map.put(key, value);
		}

		return map;
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class json {
		public static String toStr(Object obj) {
			try {
				return getObjectMapper().writeValueAsString(obj);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return null;
			}
		}

		public static Map<String, Object> toMap(String jsonStr) {
			try {
				return getObjectMapper().readValue(jsonStr, LinkedHashMap.class);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return Collections.emptyMap();
			}
		}
	}
}