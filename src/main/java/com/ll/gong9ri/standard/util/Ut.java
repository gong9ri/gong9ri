package com.ll.gong9ri.standard.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Ut {
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
}