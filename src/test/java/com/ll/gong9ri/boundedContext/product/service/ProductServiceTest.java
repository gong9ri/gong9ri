package com.ll.gong9ri.boundedContext.product.service;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import com.ll.gong9ri.boundedContext.member.entity.AuthLevel;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.product.dto.ProductDiscountDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDetailDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductRegisterDTO;
import com.ll.gong9ri.boundedContext.product.dto.SearchDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductDiscount;
import com.ll.gong9ri.boundedContext.store.entity.Store;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class ProductServiceTest {
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductDiscountService productDiscountService;

	private Member member;
	private Store store;

	@BeforeEach
	void setup() {
		member = Member.builder()
			.id(9879878L)
			.authLevel(AuthLevel.STORE)
			.username("SDFSDFSDF@#$")
			.build();

		store = Store.builder()
			.id(4365640L)
			.name("#$%%$#%$#3")
			.member(member)
			.build();
	}

	@Test
	@DisplayName("product registration test")
	void productRegistrationTest() {
		List<Integer> headCountList = new ArrayList<>();
		headCountList.add(10);
		List<Integer> discountRateList = new ArrayList<>();
		discountRateList.add(10);

		ProductRegisterDTO registerDTO = ProductRegisterDTO.builder()
			.name("버튼 카라 반팔 니트")
			.description("여름에도 시원하게 입을 수 있는 반팔 니트 입니다.")
			.price(43000)
			.maxPurchaseNum(2)
			.build();

		RsData<Product> productRs = productService.registerProduct(store, registerDTO);

		assertThat(productRs.isSuccess()).isTrue();
		assertThat(productRs.getData().getName()).isEqualTo(registerDTO.getName());
		assertThat(productRs.getData().getProductOptions()).isEmpty();
	}

	@Test
	@DisplayName("adding product options")
	void addProductOptionsTest() {
		List<String> colorOptionList = new ArrayList<>();
		colorOptionList.add("블랙");
		colorOptionList.add("화이트");
		colorOptionList.add("아이보리");
		colorOptionList.add("네이비");

		List<String> sizeOptionList = new ArrayList<>();
		sizeOptionList.add("s");
		sizeOptionList.add("m");
		sizeOptionList.add("l");
		sizeOptionList.add("xl");

		List<ProductOptionDetailDTO> optionNameDTOList = colorOptionList.stream()
			.flatMap(color -> sizeOptionList.stream()
				.map(size -> ProductOptionDetailDTO.builder()
					.optionDetail(color + " " + size)
					.build()))
			.toList();

		List<Integer> headCountList = new ArrayList<>();
		headCountList.add(10);
		List<Integer> discountPriceList = new ArrayList<>();
		discountPriceList.add(10);

		List<ProductDiscountDTO> discountDTOList = headCountList.stream()
			.flatMap(headCount -> discountPriceList.stream()
				.map(discount -> ProductDiscountDTO.builder()
					.headCount(headCount)
					.salePrice(discount)
					.build()))
			.toList();

		ProductRegisterDTO productDTO =
			ProductRegisterDTO.builder()
				.name("버튼 카라 반팔 니트")
				.description("여름에도 시원하게 입을 수 있는 반팔 니트 입니다.")
				.price(43000)
				.maxPurchaseNum(2)
				.build();

		ProductOptionDTO productOptionDTO = ProductOptionDTO.builder()
			.optionDetails(optionNameDTOList)
			.build();

		RsData<Product> productRs = productService.registerProduct(store, productDTO);
		discountDTOList.forEach(e -> productDiscountService.create(productRs.getData(), e));
		RsData<Product> addOptionRs = productService.addOptions(productRs.getData().getId(), productOptionDTO);

		assertThat(productRs.isSuccess()).isTrue();
		assertThat(addOptionRs.isSuccess()).isTrue();
		assertThat(productRs.getData().getName()).isEqualTo(productDTO.getName());
		assertThat(productRs.getData().getProductOptions()).hasSize(colorOptionList.size() * sizeOptionList.size());
	}

	@Test
	@DisplayName("adding single product option")
	void addSingleProductOptionTest() {
		List<String> colorOptionList = new ArrayList<>();
		colorOptionList.add("블랙");
		colorOptionList.add("화이트");
		colorOptionList.add("아이보리");
		colorOptionList.add("네이비");

		List<ProductOptionDetailDTO> optionNameDTOList = colorOptionList.stream()
			.map(e -> ProductOptionDetailDTO.builder()
				.optionDetail(e)
				.build())
			.toList();

		List<Integer> headCountList = new ArrayList<>();
		headCountList.add(10);
		List<Integer> discountRateList = new ArrayList<>();
		discountRateList.add(10);

		ProductRegisterDTO registerDTO = ProductRegisterDTO.builder()
			.name("버튼 카라 반팔 니트")
			.description("여름에도 시원하게 입을 수 있는 반팔 니트 입니다.")
			.price(43000)
			.maxPurchaseNum(2)
			.build();

		ProductOptionDTO productOptionDTO = ProductOptionDTO.builder()
			.optionDetails(optionNameDTOList)
			.build();

		RsData<Product> productRs = productService.registerProduct(store, registerDTO);
		RsData<Product> addOptionRs = productService.addOptions(productRs.getData().getId(), productOptionDTO);

		assertThat(productRs.isSuccess()).isTrue();
		assertThat(addOptionRs.isSuccess()).isTrue();
		assertThat(productRs.getData().getName()).isEqualTo(registerDTO.getName());
		assertThat(productRs.getData().getProductOptions()).hasSize(colorOptionList.size());
	}

	@Test
	@DisplayName("search for all products")
	void getAllProducts() {
		List<String> colorOptionList = new ArrayList<>();
		colorOptionList.add("블랙");
		colorOptionList.add("화이트");
		colorOptionList.add("아이보리");
		colorOptionList.add("네이비");

		List<ProductOptionDetailDTO> optionNameDTOList = colorOptionList.stream()
			.map(e -> ProductOptionDetailDTO.builder()
				.optionDetail(e)
				.build())
			.toList();

		List<Integer> headCountList = new ArrayList<>();
		headCountList.add(10);
		List<Integer> discountRateList = new ArrayList<>();
		discountRateList.add(10);

		ProductRegisterDTO productDTO = ProductRegisterDTO.builder()
			.name("버튼 카라 반팔 니트")
			.description("여름에도 시원하게 입을 수 있는 반팔 니트 입니다.")
			.price(43000)
			.maxPurchaseNum(2)
			.build();

		ProductOptionDTO productOptionDTO = ProductOptionDTO.builder()
			.optionDetails(optionNameDTOList)
			.build();

		RsData<Product> productRs = productService.registerProduct(store, productDTO);
		productService.addOptions(productRs.getData().getId(), productOptionDTO);

		RsData<List<Product>> getAllProductsRs = productService.getAllProducts();

		assertThat(getAllProductsRs.isSuccess()).isTrue();
		assertThat(getAllProductsRs.getData()).isNotEmpty();
	}

	@Test
	@DisplayName("search all products")
	void showAllProductsTest() {
		List<Integer> headCountList = new ArrayList<>();
		headCountList.add(10);
		List<Integer> discountRateList = new ArrayList<>();
		discountRateList.add(10);

		ProductRegisterDTO[] productRegisterDTOs = {
			ProductRegisterDTO.builder()
				.name("반팔 티셔츠")
				.price(15000)
				.build(),
			ProductRegisterDTO.builder()
				.name("긴팔 티셔츠")
				.price(20000)
				.build(),
			ProductRegisterDTO.builder()
				.name("반바지")
				.price(20000)
				.build()
		};

		int preRegistrationProductCnt = productService.getAllProducts().getData().size();

		Arrays.stream(productRegisterDTOs).forEach(o -> productService.registerProduct(store, o));

		RsData<List<Product>> getAllProductsRs = productService.getAllProducts();

		assertThat(getAllProductsRs.isSuccess()).isTrue();
		assertThat(getAllProductsRs.getData()).hasSize(preRegistrationProductCnt + productRegisterDTOs.length);
	}

	@Test
	@DisplayName("search product test")
	void searchProductTest() {
		List<Integer> headCountList = new ArrayList<>();
		headCountList.add(10);
		List<Integer> discountRateList = new ArrayList<>();
		discountRateList.add(10);

		ProductRegisterDTO[] productRegisterDTOs = {
			ProductRegisterDTO.builder()
				.name("반팔 티셔츠")
				.price(15000)
				.build(),
			ProductRegisterDTO.builder()
				.name("긴팔 티셔츠")
				.price(20000)
				.build(),
			ProductRegisterDTO.builder()
				.name("반바지")
				.price(20000)
				.build()
		};

		Arrays.stream(productRegisterDTOs).forEach(o -> productService.registerProduct(store, o));

		SearchDTO searchDTO = SearchDTO.builder()
			.keyword("티셔츠")
			.build();

		RsData<List<Product>> searchRs = productService.search(searchDTO);

		assertThat(searchRs.isSuccess()).isTrue();
		assertThat(searchRs.getData()).hasSize(2);
	}

	@Test
	@DisplayName("save product's discount price")
	void discountRateSaveTest() {
		ProductRegisterDTO registerDTO = ProductRegisterDTO.builder()
			.name("반팔 티셔츠")
			.price(15000)
			.build();

		ProductDiscountDTO productDiscountDTO =
			ProductDiscountDTO.builder()
				.headCount(10)
				.salePrice(5)
				.build();

		RsData<Product> productRs = productService.registerProduct(store, registerDTO);

		RsData<ProductDiscount> productDiscountRs = productDiscountService.create(productRs.getData(),
			productDiscountDTO);
		assertThat(productDiscountRs.isSuccess()).isTrue();

		Optional<ProductDiscount> oProductDiscount = productDiscountService.findById(
			productDiscountRs.getData().getId());
		assertThat(oProductDiscount)
			.isPresent()
			.contains(productDiscountRs.getData());
	}
}