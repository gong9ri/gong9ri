package com.ll.gong9ri.base.tosspayments.tossConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
public class TossConfig {
	@Getter
	private static String CLIENT_KEY;

	@Getter
	private static String SECRET_KEY;
	@Getter
	private static String ORDER_ID_PREFIX;

	@Value("${custom.toss.clientKey}")
	public void setClientKey(String clientKey) {
		TossConfig.CLIENT_KEY = clientKey;
	}

	@Value("${custom.toss.secretKey}")
	public void setSecretKey(String secretKey) {
		TossConfig.SECRET_KEY = secretKey;
	}

	@Value("${custom.orderIdPrefix}")
	public void setOrderIdPrefix(String orderIdPrefix) {
		TossConfig.ORDER_ID_PREFIX = orderIdPrefix;
	}
}
