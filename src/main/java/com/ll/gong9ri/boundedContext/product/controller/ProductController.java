package com.ll.gong9ri.boundedContext.product.controller;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.service.ProductService;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import com.ll.gong9ri.boundedContext.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final StoreService storeService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/registration")
    public String showProductRegistration() {
        return "product/productRegistration";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/registration")
    public String registerProduct(final ProductDTO productDTO) {
        Member actor = rq.getMember();
        Optional<Store> store = storeService.findByMember(actor);

        if (store.isEmpty()) {
            // TODO: 테스트를 위한 코드. 삭제할 것.
            storeService.create(actor, "G9");
            store = storeService.findByMember(actor);

//            return rq.historyBack("등록된 스토어 정보가 없습니다.");
        }

        RsData<Product> productRs = productService.registerProduct(store.get(), productDTO);
        if (productRs.isFail())
            return rq.historyBack(productRs);
        return rq.redirectWithMsg("/product/registration", productRs.getMsg());
    }
}
