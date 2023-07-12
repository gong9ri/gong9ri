package com.ll.gong9ri.boundedContext.tosspayment.service;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.AuthLevel;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.entity.ProviderTypeCode;
import com.ll.gong9ri.boundedContext.order.dto.OrderRecipientDTO;
import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;
import com.ll.gong9ri.boundedContext.order.entity.ProductOptionQuantity;
import com.ll.gong9ri.boundedContext.order.service.OrderInfoService;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class PaymentServiceTest {
	@Autowired
	private OrderInfoService orderInfoService;

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
		ProductOptionQuantity productOptionQuantity = ProductOptionQuantity.builder()
			.optionId(product.getProductOptions().get(0).getId())
			.optionDetail(product.getProductOptions().get(0).getOptionDetail())
			.quantity(3)
			.build();
		RsData<OrderInfo> rsCreate = orderInfoService.create(member, product, List.of(productOptionQuantity));
		assertThat(rsCreate.isSuccess()).isTrue();

		final OrderRecipientDTO orderRecipientDTO = OrderRecipientDTO.builder()
			.recipient(member.getUsername())
			.mainAddress("화성 올림푸스화산")
			.subAddress("지하 주차장")
			.phoneNumber("010-1234-5678")
			.build();

		final RsData<OrderInfo> rsConfirmOrderInfo = orderInfoService.confirm(
			rsCreate.getData(),
			orderRecipientDTO
		);

		assertThat(rsConfirmOrderInfo.isSuccess()).isTrue();
	}
}
