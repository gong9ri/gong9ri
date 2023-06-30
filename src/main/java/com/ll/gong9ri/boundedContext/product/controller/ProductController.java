package com.ll.gong9ri.boundedContext.product.controller;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDTO;
import com.ll.gong9ri.boundedContext.product.dto.SearchDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

//TODO: hasAuthority('store') 추가
@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private static final String PRODUCTS = "products";
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

        session.setAttribute("product", productRs.getData().toDTO());

        return rq.redirectWithMsg("/product/option", productRs.getMsg());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/option")
    public String showProductOption() {
        return "product/optionDetails";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/option")
    public String addProductOptions(ProductOptionDTO productOptionDTO) {
        HttpSession session = rq.getSession();
        Product product = ((ProductDTO) session.getAttribute("product")).toEntity();

        RsData<Product> productRs = productService.addOptionDetails(product, productOptionDTO);
        if (productRs.isFail()) {
            return rq.historyBack("상품 상세 옵션 등록에 실했습니다.");
        }

        return rq.redirectWithMsg("/product/list", productRs);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String showProducts(Model model) {
        RsData<List<Product>> getRs = productService.getAllProducts();

        if (getRs.isFail()) {
            return rq.historyBack("상품 목록을 가져오는 데 실패했습니다.");
        }

        sendProductListToView(model, getRs);
        return "product/productList";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search")
    public String showSearchList(Model model, SearchDTO searchDTO) {
        RsData<List<Product>> searchRs = productService.search(searchDTO);

        if (searchRs.isFail()) {
            return rq.historyBack("검색 결과를 가져오는 데 실패했습니다.");
        }

        sendProductListToView(model, searchRs);
        return "product/searchForm";
    }

    private void sendProductListToView(Model model, RsData<List<Product>> rsData) {
        List<Product> products = rsData.getData();

        List<ProductDTO> productDTOList = products.stream().map(Product::toDTO).toList();

        model.addAttribute(PRODUCTS, productDTOList);
    }
}
