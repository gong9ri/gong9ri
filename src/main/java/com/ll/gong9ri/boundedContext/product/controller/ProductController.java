package com.ll.gong9ri.boundedContext.product.controller;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/store/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final Rq rq;

    //    @PreAuthorize("isAuthenticated()")
    @GetMapping("/registration")
    public String showProductRegistration() {
        return "product/productRegistration";
    }

    //    @PreAuthorize("isAuthenticated()")
    @PostMapping("/registration")
    public String registerProduct(final ProductDTO productDTO) {
        RsData<Product> productRs = productService.registerProduct(productDTO);
        if (productRs.isFail())
            return rq.historyBack(productRs);
        return rq.redirectWithMsg("/store/product/registration", productRs.getMsg());
    }
}
