package com.ll.gong9ri.boundedContext.product.service;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.dto.*;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductDiscount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductDiscountService productDiscountService;

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

        RsData<Product> productRs = productService.registerProduct(registerDTO);

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

        List<ProductOptionNameDTO> optionNameDTOList = colorOptionList.stream()
                .flatMap(color -> sizeOptionList.stream()
                        .map(size -> ProductOptionNameDTO.builder()
                                .optionOneName(color)
                                .optionTwoName(size)
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
                .optionOne("색상")
                .optionTwo("사이즈")
                .optionNames(optionNameDTOList)
                .build();

        RsData<Product> productRs = productService.registerProduct(productDTO);
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

        List<ProductOptionNameDTO> optionNameDTOList = colorOptionList.stream()
                .map(e -> ProductOptionNameDTO.builder()
                        .optionTwoName(e)
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
                .optionOne("색상")
                .optionNames(optionNameDTOList)
                .build();

        RsData<Product> productRs = productService.registerProduct(registerDTO);
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

        List<ProductOptionNameDTO> optionNameDTOList = colorOptionList.stream()
                .map(e -> ProductOptionNameDTO.builder()
                        .optionTwoName(e)
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
                .optionOne("색상")
                .optionNames(optionNameDTOList)
                .build();

        RsData<Product> productRs = productService.registerProduct(productDTO);
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

        Arrays.stream(productRegisterDTOs).forEach(productService::registerProduct);

        RsData<List<Product>> getAllProductsRs = productService.getAllProducts();

        assertThat(getAllProductsRs.isSuccess()).isTrue();
        assertThat(getAllProductsRs.getData()).hasSize(productRegisterDTOs.length);
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

        Arrays.stream(productRegisterDTOs).forEach(productService::registerProduct);

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

        RsData<Product> productRs = productService.registerProduct(registerDTO);

        RsData<ProductDiscount> productDiscountRs = productDiscountService.create(productRs.getData(), productDiscountDTO);
        assertThat(productDiscountRs.isSuccess()).isTrue();

        Optional<ProductDiscount> oProductDiscount = productDiscountService.findById(productDiscountRs.getData().getId());
        assertThat(oProductDiscount)
                .isPresent()
                .contains(productDiscountRs.getData());
    }
}