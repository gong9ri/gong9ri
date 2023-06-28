package com.ll.gong9ri.boundedContext.product.service;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("product registration test")
    void productRegistrationTest() {
        ProductDTO productDTO = ProductDTO.builder()
                .name("버튼 카라 반팔 니트")
                .description("여름에도 시원하게 입을 수 있는 반팔 니트 입니다.")
                .price(43000)
                .maxPurchaseNum(2)
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

        ProductDTO productDTO = ProductDTO.builder()
                .name("버튼 카라 반팔 니트")
                .description("여름에도 시원하게 입을 수 있는 반팔 니트 입니다.")
                .price(43000)
                .maxPurchaseNum(2)
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

        ProductDTO productDTO = ProductDTO.builder()
                .name("버튼 카라 반팔 니트")
                .description("여름에도 시원하게 입을 수 있는 반팔 니트 입니다.")
                .price(43000)
                .maxPurchaseNum(2)
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

        ProductDTO productDTO = ProductDTO.builder()
                .name("버튼 카라 반팔 니트")
                .description("여름에도 시원하게 입을 수 있는 반팔 니트 입니다.")
                .price(43000)
                .maxPurchaseNum(2)
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
}