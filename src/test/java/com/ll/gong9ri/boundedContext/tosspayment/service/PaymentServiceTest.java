package com.ll.gong9ri.boundedContext.tosspayment.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.tosspayments.entity.PaymentCreateBody;
import com.ll.gong9ri.base.tosspayments.entity.PaymentResult;
import com.ll.gong9ri.base.tosspayments.entity.PaymentWebClient;
import com.ll.gong9ri.base.tosspayments.service.PaymentService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class PaymentServiceTest {
	@Autowired
	private PaymentService paymentService;

	@Test
	@DisplayName("Create Payment Test")
	void createPaymentTest() {
		PaymentCreateBody paymentCreateBody = PaymentCreateBody.builder()
			.method("카드")
			.amount(1000000)
			.orderId("a4CWyWY5m89PNh7xJwhk1")
			.orderName("pattern T shrit")
			.build();

		PaymentResult result = PaymentWebClient.paymentCreate(paymentCreateBody);
		assertThat(result).isNotNull();
		System.out.println(result);
	}
}