package com.ll.gong9ri.base.tosspayments.entity;

import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.ll.gong9ri.base.appConfig.AppConfig;
import com.ll.gong9ri.base.tosspayments.tossConfig.TossConfig;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentWebClient {
	private static final String BASE_URL = "https://api.tosspayments.com/v1/";
	private static final String BASE_PAYMENT = "payments";
	private static final String BASE_SUCCESS_URL = AppConfig.getBaseUrl() + "/" + BASE_PAYMENT + "/success";
	private static final String BASE_FAIL_URL = AppConfig.getBaseUrl() + "/" + BASE_PAYMENT + "/fail";

	private static String getEncodedAuth() {
		return Base64.getEncoder().encodeToString((TossConfig.getSECRET_KEY() + ":").getBytes());
	}

	private static WebClient createWebClient() {
		return WebClient.builder()
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.defaultHeader("Authorization", "Basic " + getEncodedAuth())
			.build();
	}

	/**
	 * PaymentInfo를 사용하여 API에 결제 요청을 전송하고 결과 반환하는 메소드
	 * @param paymentCreateBody
	 * @return 결제 생성 결과 입니다.
	 */
	public static PaymentResult paymentCreate(PaymentCreateBody paymentCreateBody) {
		paymentCreateBody = paymentCreateBody.toBuilder()
			.successUrl(BASE_SUCCESS_URL)
			.failUrl(BASE_FAIL_URL)
			.build();

		return createWebClient()
			.method(HttpMethod.POST)
			.uri(BASE_URL + BASE_PAYMENT)
			.bodyValue(paymentCreateBody)
			.retrieve()
			.bodyToMono(PaymentResult.class)
			.block();
	}

	public static PaymentResult paymentRequest(PaymentRequestBody paymentRequestBody) {
		paymentRequestBody = paymentRequestBody.toBuilder()
			.successUrl(BASE_SUCCESS_URL)
			.failUrl(BASE_FAIL_URL)
			.build();

		return createWebClient()
			.method(HttpMethod.POST)
			.uri(BASE_URL + BASE_PAYMENT)
			.bodyValue(paymentRequestBody)
			.retrieve()
			.bodyToMono(PaymentResult.class)
			.block();
	}
}
