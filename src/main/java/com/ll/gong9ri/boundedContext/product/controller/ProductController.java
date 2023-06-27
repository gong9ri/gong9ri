package com.ll.gong9ri.boundedContext.product.controller;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//TODO: hasAuthority('store') 추가
@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/registration")
    public String showProductRegistration() {
        return "product/productRegistration";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/registration")
    public String registerProduct(ProductDTO productDTO) {
        HttpSession session = rq.getSession();
        RsData<Product> productRs = productService.registerProduct(productDTO);
        if (productRs.isFail())
            return rq.historyBack(productRs);

        session.setAttribute("product", productRs.getData());

        return rq.redirectWithMsg("/product/registration/option", productRs.getMsg());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/registration/option")
    public String showProductOption() {
        return "product/optionDetails";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/registration/option")
    public String addProductOptions(ProductOptionDTO productOptionDTO) {
        HttpSession session = rq.getSession();
        Product product = (Product) session.getAttribute("product");

        RsData<Product> productRs = productService.addOptionDetails(product, productOptionDTO);
        if (productRs.isFail()) {
            return rq.historyBack("상품 상세 옵션 등록에 실했습니다.");
        }

        return rq.redirectWithMsg("/product/registration", productRs);
    }
}
