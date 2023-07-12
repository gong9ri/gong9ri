package com.ll.gong9ri.base.initData;

import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.service.MemberService;
import com.ll.gong9ri.boundedContext.product.dto.ProductDiscountDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDetailDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductRegisterDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.service.ProductDiscountService;
import com.ll.gong9ri.boundedContext.product.service.ProductOptionService;
import com.ll.gong9ri.boundedContext.product.service.ProductService;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import com.ll.gong9ri.boundedContext.store.service.StoreService;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
	@Bean
	CommandLineRunner initData(
		MemberService memberService,
		StoreService storeService,
		ProductService productService,
		ProductOptionService productOptionService,
		ProductDiscountService productDiscountService
	) {
		return args -> {
			final RsData<Member> rsStoreMember = memberService.storeJoin("apple", "1234");
			memberService.join("user1", "1234");
			final Store store = storeService.findByMemberId(rsStoreMember.getData().getId()).orElseThrow();
			Product[] products = IntStream.range(1, 4)
				.mapToObj(i -> ProductRegisterDTO.builder()
					.name("iPhone " + i + " 세대")
					.price(i * 10000)
					.description("iPhone" + i + "는 위대한 제품입니다")
					.maxPurchaseNum(5 % i)
					.build()
				)
				.map(o -> productService.registerProduct(store, o))
				.map(RsData::getData)
				.toArray(Product[]::new);

			ProductOptionDetailDTO defaultOption = productOptionService.getProductOptions(products[0].getId()).get(0);

			productService.addOptions(products[0].getId(), ProductOptionDTO.builder()
				.optionName("")
				.optionDetails(
					LongStream.range(1L, 3L)
						.mapToObj(i -> ProductOptionDetailDTO.builder()
							.id(i == 0 ? defaultOption.getId() : null) // TODO: update service
							.optionDetail("RED + " + (i * 100) + "GB")
							.build())
						.toList())
				.build());

			IntStream.range(1, 3)
				.forEach(i -> productDiscountService.create(
					products[0],
					ProductDiscountDTO.builder()
						.headCount(10 * i)
						.salePrice(products[0].getPrice() - (i * 1000))
						.build())
				);

			productService.addOptions(products[1].getId(), ProductOptionDTO.builder()
				.optionName("")
				.optionDetails(
					LongStream.range(1L, 3L)
						.mapToObj(i -> ProductOptionDetailDTO.builder()
							.id(i == 0 ? defaultOption.getId() : null) // TODO: update service
							.optionDetail("GREEN + " + (i * 100) + "GB")
							.build())
						.toList())
				.build());

			IntStream.range(1, 3)
				.forEach(i -> productDiscountService.create(
					products[1],
					ProductDiscountDTO.builder()
						.headCount(10 * i)
						.salePrice(products[1].getPrice() - (i * 1000))
						.build())
				);

			productService.addOptions(products[2].getId(), ProductOptionDTO.builder()
				.optionName("")
				.optionDetails(
					LongStream.range(1L, 3L)
						.mapToObj(i -> ProductOptionDetailDTO.builder()
							.id(i == 0 ? defaultOption.getId() : null) // TODO: update service
							.optionDetail("BLUE + " + (i * 100) + "GB")
							.build())
						.toList())
				.build());

			IntStream.range(1, 3)
				.forEach(i -> productDiscountService.create(
					products[2],
					ProductDiscountDTO.builder()
						.headCount(10 * i)
						.salePrice(products[2].getPrice() - (i * 1000))
						.build())
				);
		};
	}
}