package com.ll.gong9ri.boundedContext.product.controller;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.service.ProductService;
import com.ll.gong9ri.boundedContext.store.service.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String registerProduct(ProductDTO productDTO, HttpServletRequest request) {
        HttpSession session = request.getSession();
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
    public String addProductOptions(ProductOptionDTO productOptionDTO, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Product product = (Product) session.getAttribute("product");

        RsData<Product> productRs = productService.addOptionDetails(product, productOptionDTO);
        if (productRs.isFail()) {
            return rq.historyBack("상품 상세 옵션 등록에 실했습니다.");
        }

        return rq.redirectWithMsg("/product/registration", productRs);
    }


}
