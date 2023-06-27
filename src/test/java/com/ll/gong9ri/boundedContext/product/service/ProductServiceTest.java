package com.ll.gong9ri.boundedContext.product.service;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.service.MemberService;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import com.ll.gong9ri.boundedContext.store.service.StoreService;
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
    private MemberService memberService;
    @Autowired
    private StoreService storeService;

    @Test
    @DisplayName("product registration test")
    void productRegistrationTest() {
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
                .optionOneName("색상")
                .optionTwoName("사이즈")
                .optionOneDetails(colorOptionList)
                .optionTwoDetails(sizeOptionList)
                .build();

        final String username = "s211r";
        final String password = "1234";
        RsData<Member> rsMember = memberService.storeJoin(username, password);

        Optional<Store> oStore = storeService.findByMemberId(rsMember.getData().getId());
        assertThat(oStore).isNotEmpty();
        RsData<Product> productRs = productService.registerProduct(oStore.get(), productDTO);

        assertThat(productRs.isSuccess()).isTrue();
        assertThat(productRs.getData().getStore().getName()).isEqualTo(oStore.get().getName());
        assertThat(productRs.getData().getName()).isEqualTo(productDTO.getName());
        assertThat(productRs.getData().getProductOptions()).hasSize(colorOptionList.size() * sizeOptionList.size());
    }

    @Test
    @DisplayName("Test product registration without options")
    void productRegistrationWithoutOptionsTest() {
        ProductDTO productDTO = ProductDTO.builder()
                .name("버튼 카라 반팔 니트")
                .description("여름에도 시원하게 입을 수 있는 반팔 니트 입니다.")
                .price(43000)
                .maxPurchaseNum(2)
                .optionOneName("색상")
                .optionTwoName("사이즈")
                .build();

        final String username = "s211r";
        final String password = "1234";
        RsData<Member> rsMember = memberService.storeJoin(username, password);

        Optional<Store> oStore = storeService.findByMemberId(rsMember.getData().getId());
        assertThat(oStore).isNotEmpty();
        RsData<Product> productRs = productService.registerProduct(oStore.get(), productDTO);

        assertThat(productRs.isSuccess()).isTrue();
        assertThat(productRs.getData().getStore().getName()).isEqualTo(oStore.get().getName());
        assertThat(productRs.getData().getName()).isEqualTo(productDTO.getName());
        assertThat(productRs.getData().getProductOptions()).isEmpty();
    }

    @Test
    @DisplayName("product registration includes only one option")
    void productRegistrationWithOptionOneTest() {
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
                .optionOneName("색상")
                .optionTwoName("사이즈")
                .optionOneDetails(colorOptionList)
                .build();

        final String username = "s211r";
        final String password = "1234";
        RsData<Member> rsMember = memberService.storeJoin(username, password);

        Optional<Store> oStore = storeService.findByMemberId(rsMember.getData().getId());
        assertThat(oStore).isNotEmpty();
        RsData<Product> productRs = productService.registerProduct(oStore.get(), productDTO);

        assertThat(productRs.isSuccess()).isTrue();
        assertThat(productRs.getData().getStore().getName()).isEqualTo(oStore.get().getName());
        assertThat(productRs.getData().getName()).isEqualTo(productDTO.getName());
        assertThat(productRs.getData().getProductOptions()).hasSize(colorOptionList.size());
    }
}