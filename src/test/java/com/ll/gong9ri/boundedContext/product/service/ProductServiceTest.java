package com.ll.gong9ri.boundedContext.product.service;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDTO;
import com.ll.gong9ri.boundedContext.product.dto.SearchDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductDiscount;
import com.ll.gong9ri.boundedContext.product.repository.ProductDiscountRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductDiscountRepository productDiscountRepository;

    @Test
    @DisplayName("product registration test")
    void productRegistrationTest() {
        List<Integer> headCountList = new ArrayList<>();
        headCountList.add(10);
        List<Integer> discountRateList = new ArrayList<>();
        discountRateList.add(10);

        ProductDTO productDTO = ProductDTO.builder()
                .name("버튼 카라 반팔 니트")
                .description("여름에도 시원하게 입을 수 있는 반팔 니트 입니다.")
                .price(43000)
                .maxPurchaseNum(2)
                .headCounts(headCountList)
                .salePrices(discountRateList)
                .build();

        RsData<Product> productRs = productService.registerProduct(productDTO);

        assertThat(productRs.isSuccess()).isTrue();
        assertThat(productRs.getData().getName()).isEqualTo(productDTO.getName());
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


        List<Integer> headCountList = new ArrayList<>();
        headCountList.add(10);
        List<Integer> discountRateList = new ArrayList<>();
        discountRateList.add(10);

        ProductDTO productDTO = ProductDTO.builder()
                .name("버튼 카라 반팔 니트")
                .description("여름에도 시원하게 입을 수 있는 반팔 니트 입니다.")
                .price(43000)
                .maxPurchaseNum(2)
                .headCounts(headCountList)
                .salePrices(discountRateList)
                .build();

        ProductOptionDTO productOptionDTO = ProductOptionDTO.builder()
                .optionOneName("색상")
                .optionTwoName("사이즈")
                .optionOneDetails(colorOptionList)
                .optionTwoDetails(sizeOptionList)
                .build();

        RsData<Product> productRs = productService.registerProduct(productDTO);
        RsData<Product> addOptionRs = productService.addOptionDetails(productRs.getData(), productOptionDTO);

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

        List<Integer> headCountList = new ArrayList<>();
        headCountList.add(10);
        List<Integer> discountRateList = new ArrayList<>();
        discountRateList.add(10);

        ProductDTO productDTO = ProductDTO.builder()
                .name("버튼 카라 반팔 니트")
                .description("여름에도 시원하게 입을 수 있는 반팔 니트 입니다.")
                .price(43000)
                .maxPurchaseNum(2)
                .headCounts(headCountList)
                .salePrices(discountRateList)
                .build();

        ProductOptionDTO productOptionDTO = ProductOptionDTO.builder()
                .optionOneName("색상")
                .optionOneDetails(colorOptionList)
                .build();

        RsData<Product> productRs = productService.registerProduct(productDTO);
        RsData<Product> addOptionRs = productService.addOptionDetails(productRs.getData(), productOptionDTO);

        assertThat(productRs.isSuccess()).isTrue();
        assertThat(addOptionRs.isSuccess()).isTrue();
        assertThat(productRs.getData().getName()).isEqualTo(productDTO.getName());
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

        List<Integer> headCountList = new ArrayList<>();
        headCountList.add(10);
        List<Integer> discountRateList = new ArrayList<>();
        discountRateList.add(10);

        ProductDTO productDTO = ProductDTO.builder()
                .name("버튼 카라 반팔 니트")
                .description("여름에도 시원하게 입을 수 있는 반팔 니트 입니다.")
                .price(43000)
                .maxPurchaseNum(2)
                .headCounts(headCountList)
                .salePrices(discountRateList)
                .build();

        ProductOptionDTO productOptionDTO = ProductOptionDTO.builder()
                .optionOneName("색상")
                .optionOneDetails(colorOptionList)
                .build();

        RsData<Product> productRs = productService.registerProduct(productDTO);
        productService.addOptionDetails(productRs.getData(), productOptionDTO);

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

        ProductDTO[] products = {
                ProductDTO.builder()
                        .name("반팔 티셔츠")
                        .price(15000)
                        .headCounts(headCountList)
                        .salePrices(discountRateList)
                        .build(),
                ProductDTO.builder()
                        .name("긴팔 티셔츠")
                        .price(20000)
                        .headCounts(headCountList)
                        .salePrices(discountRateList)
                        .build(),
                ProductDTO.builder()
                        .name("반바지")
                        .price(20000)
                        .headCounts(headCountList)
                        .salePrices(discountRateList)
                        .build()
        };

        Arrays.stream(products).forEach(productService::registerProduct);

        RsData<List<Product>> getAllProductsRs = productService.getAllProducts();

        assertThat(getAllProductsRs.isSuccess()).isTrue();
        assertThat(getAllProductsRs.getData()).hasSize(products.length);
    }

    @Test
    @DisplayName("search product test")
    void searchProductTest() {
        List<Integer> headCountList = new ArrayList<>();
        headCountList.add(10);
        List<Integer> discountRateList = new ArrayList<>();
        discountRateList.add(10);

        ProductDTO[] products = {
                ProductDTO.builder()
                        .name("반팔 티셔츠")
                        .price(15000)
                        .headCounts(headCountList)
                        .salePrices(discountRateList)
                        .build(),
                ProductDTO.builder()
                        .name("긴팔 티셔츠")
                        .price(20000)
                        .headCounts(headCountList)
                        .salePrices(discountRateList)
                        .build(),
                ProductDTO.builder()
                        .name("반바지")
                        .price(20000)
                        .headCounts(headCountList)
                        .salePrices(discountRateList)
                        .build()
        };

        Arrays.stream(products).forEach(productService::registerProduct);

        SearchDTO searchDTO = SearchDTO.builder()
                .keyword("티셔츠")
                .build();

        RsData<List<Product>> searchRs = productService.search(searchDTO);

        assertThat(searchRs.isSuccess()).isTrue();
        assertThat(searchRs.getData()).hasSize(2);
    }

    @Test
    @DisplayName("save product's discountRate")
    void discountRateSaveTest() {
        List<ProductDiscount> productDiscounts = new ArrayList<>() {{
            add(ProductDiscount.builder()
                    .headCount(10)
                    .salePrice(5)
                    .build());
        }};
        RsData<List<ProductDiscount>> productRs = productService.saveProductDiscount(productDiscounts);

        assertThat(productRs.isSuccess()).isTrue();

        List<ProductDiscount> productDiscountList = productRs.getData().stream()
                .filter(e -> productDiscountRepository.findById(e.getId()).get().equals(e))
                .toList();

        assertThat(productDiscountList).hasSameSizeAs(productDiscounts);
    }
}