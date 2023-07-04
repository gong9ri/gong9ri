package com.ll.gong9ri.boundedContext.tosspayment.service;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.base.tosspayments.entity.PaymentConfirmBody;
import com.ll.gong9ri.base.tosspayments.entity.PaymentCreateBody;
import com.ll.gong9ri.base.tosspayments.entity.PaymentResult;
import com.ll.gong9ri.base.tosspayments.entity.PaymentWebClient;
import com.ll.gong9ri.base.tosspayments.service.PaymentService;
import com.ll.gong9ri.boundedContext.member.entity.AuthLevel;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.entity.ProviderTypeCode;
import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;
import com.ll.gong9ri.boundedContext.order.service.OrderService;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;
import com.ll.gong9ri.boundedContext.store.entity.Store;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class PaymentServiceTest {
	@Autowired
	private PaymentService paymentService;

	@Autowired
	private OrderService orderService;

	private Member member;
	private Store store;
	private Product product;
	private Member storeMember;

	@BeforeEach
	void beforeEach() {
		member = Member.builder()
			.id(34208L)
			.username("yjnthbrg")
			.providerTypeCode(ProviderTypeCode.GONG9RI)
			.authLevel(AuthLevel.MEMBER)
			.build();

		storeMember = Member.builder()
			.id(1234L)
			.username("ersdfns")
			.providerTypeCode(ProviderTypeCode.GONG9RI)
			.authLevel(AuthLevel.STORE)
			.build();

		store = Store.builder()
			.id(78787L)
			.member(storeMember)
			.name("awdsnfg")
			.build();

		final Product initProduct = Product.builder()
			.id(9810L)
			.store(store)
			.name("dfvozin")
			.price(10000)
			.optionOne("s")
			.optionTwo("red")
			.build();

		List<ProductOption> options = LongStream.range(3L, 12L)
			.mapToObj(l -> ProductOption.builder()
				.product(initProduct)
				.optionOneName("asdas" + l)
				.optionOneName("iojptr" + l)
				.build())
			.collect(Collectors.toList());
		product = initProduct.toBuilder()
				.productOptions(options)
			.build();
	}

	@Test
	@DisplayName("Create Payment Test")
	void createPaymentTest() {
		OrderInfo orderInfo = OrderInfo.builder()
			.id(523L)
			.memberId(2334L)
			.username("aknjfd")
			.storeId(6342L)
			.storeName("oguvh")
			.productId(777L)
			.productName("svdsd")
			.build();

		PaymentResult result = paymentService.createPayment(orderInfo).getData();
		assertThat(result).isNotNull();
		System.out.println(result);
	}

	@Test
	@DisplayName("Create Order Test")
	void createOrderTest() {
		// TODO: 주문 생성후 승인 받기
		RsData<OrderInfo> rsCreateOrderInfo = orderService.createOrder(member, product);
		assertThat(rsCreateOrderInfo.isSuccess()).isTrue();

		final Map<ProductOption, Integer> options = new HashMap<>();
		options.put(product.getProductOptions().get(1), 3);
		options.put(product.getProductOptions().get(4), 1);
		options.put(product.getProductOptions().get(5), 2);
		RsData<OrderInfo> rsConfirmOrderInfo = orderService.confirmOrder(
			rsCreateOrderInfo.getData().getMemberId(),
			rsCreateOrderInfo.getData().getId(),
			options);
		assertThat(rsConfirmOrderInfo.isSuccess()).isTrue();
	}

	@Test
	@DisplayName("Payment Confirm Test")
	void paymentConfirmTest() {
		final Integer amount = 1000000;

		PaymentCreateBody paymentCreateBody = PaymentCreateBody.builder()
			.method("카드")
			.amount(amount)
			.orderId("a4CWyWY5m89PNh7xJwhk1")
			.orderName("pattern T shrit")
			.build();

		PaymentResult createResult = PaymentWebClient.paymentCreate(paymentCreateBody).toEntity();
		System.out.println(createResult);
		// TODO: 실제 QR코드 찍는 과정 필요

		PaymentConfirmBody paymentConfirmBody = PaymentConfirmBody.builder()
			.paymentKey(createResult.getPaymentKey())
			.amount(amount)
			.orderId(createResult.getOrderId())
			.build();

		PaymentResult result = PaymentWebClient.paymentConfirm(paymentConfirmBody).toEntity();
		assertThat(result).isNotNull();
		System.out.println(result);
	}
}
