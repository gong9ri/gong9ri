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
import com.ll.gong9ri.base.tosspayments.entity.PaymentCreateBody;
import com.ll.gong9ri.base.tosspayments.entity.PaymentResult;
import com.ll.gong9ri.base.tosspayments.entity.PaymentWebClient;
import com.ll.gong9ri.base.tosspayments.service.PaymentService;
import com.ll.gong9ri.boundedContext.member.entity.AuthLevel;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.entity.ProviderTypeCode;
import com.ll.gong9ri.boundedContext.order.dto.OrderRecipientDTO;
import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;
import com.ll.gong9ri.boundedContext.order.service.OrderInfoService;
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
	private OrderInfoService orderInfoService;

	@Autowired
	private PaymentWebClient paymentWebClient;

	private Member member;
	private Store store;
	private Product product;
	private Member storeMember;

	@BeforeEach
	void beforeEach() {
		member = Member.builder()
			.id(Long.MAX_VALUE - 34208L)
			.username("yjnthbrg")
			.providerTypeCode(ProviderTypeCode.GONG9RI)
			.authLevel(AuthLevel.MEMBER)
			.build();

		storeMember = Member.builder()
			.id(Long.MAX_VALUE - 18L)
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
			.optionName("s red")
			.build();

		List<ProductOption> options = LongStream.range(3L, 12L)
			.mapToObj(l -> ProductOption.builder()
				.id(3477L + l)
				.product(initProduct)
				.optionDetail("asdas" + l + "iojptr" + l)
				.build())
			.collect(Collectors.toList());

		product = initProduct.toBuilder()
			.productOptions(options)
			.build();
	}

	@Test
	@DisplayName("Create Order Test")
	void createOrderTest() {
		RsData<OrderInfo> rsPreCreate = orderInfoService.preCreate(member, product);
		assertThat(rsPreCreate.isSuccess()).isTrue();

		RsData<OrderInfo> rsCreateOrderInfo = orderInfoService.create(rsPreCreate.getData());
		assertThat(rsCreateOrderInfo.isSuccess()).isTrue();

		final Map<ProductOption, Integer> options = new HashMap<>();
		options.put(product.getProductOptions().get(1), 3);
		options.put(product.getProductOptions().get(4), 1);
		options.put(product.getProductOptions().get(5), 2);

		final OrderRecipientDTO orderRecipientDTO = OrderRecipientDTO.builder()
			.recipient(member.getUsername())
			.mainAddress("화성 올림푸스화산")
			.subAddress("지하 주차장")
			.build();

		final RsData<OrderInfo> rsConfirmOrderInfo = orderInfoService.confirm(
			rsCreateOrderInfo.getData(),
			orderRecipientDTO,
			options
		);

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

		PaymentResult createResult = paymentWebClient.paymentCreate(paymentCreateBody).toEntity();
		// System.out.println(createResult);
		// // TODO: 실제 QR코드 찍는 과정 필요
		//
		// PaymentConfirmBody paymentConfirmBody = PaymentConfirmBody.builder()
		// 	.paymentKey(createResult.getPaymentKey())
		// 	.amount(amount)
		// 	.orderId(createResult.getOrderId())
		// 	.build();
		//
		// PaymentResult result = paymentWebClient.paymentConfirm(paymentConfirmBody).toEntity();
		// assertThat(result).isNotNull();
		// System.out.println(result);
	}
}
