package com.ll.gong9ri.boundedContext.product.controller;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.dto.DetailDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductDetailDTO;
import com.ll.gong9ri.boundedContext.product.dto.SearchDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
	private static final String PRODUCTS = "products";
	private static final String PRODUCT = "product";
	private final ProductService productService;
	private final Rq rq;

	private void sendProductListToView(Model model, RsData<List<Product>> rsData) {
		List<Product> products = rsData.getData();

		List<ProductDTO> productDTOList = products.stream().map(ProductDTO::toDTO).toList();

		model.addAttribute(PRODUCTS, productDTOList);
	}

	@GetMapping("/list")
	public String showProducts(Model model) {
		RsData<List<Product>> getRs = productService.getAllProducts();

		if (getRs.isFail()) {
			return rq.historyBack("상품 목록을 가져오는 데 실패했습니다.");
		}

		sendProductListToView(model, getRs);
		return "product/productList";
	}

	@GetMapping("/search")
	public String showSearchList(Model model, @Valid SearchDTO searchDTO) {
		RsData<List<Product>> searchRs = productService.search(searchDTO);

		if (searchRs.isFail()) {
			return rq.historyBack("검색 결과를 가져오는 데 실패했습니다.");
		}

		sendProductListToView(model, searchRs);
		return "product/searchForm";
	}

	@GetMapping("/details")
	public String showDetails(Model model, @Valid DetailDTO detailDTO) {
		Optional<Product> optionalProduct = productService.getProduct(detailDTO.getProductId());

		if (optionalProduct.isEmpty()) {
			return rq.historyBack("등록된 상품이 존재하지 않습니다.");
		}

		ProductDTO productDTO = ProductDTO.toDTO(optionalProduct.get());

		model.addAttribute(PRODUCT, productDTO);

		return "product/detail";
	}

	@GetMapping("/detail/{id}")
	public String detail(Model model, @PathVariable Long id) {
		final Optional<Product> optionalProduct = productService.getProduct(id);
		if (optionalProduct.isEmpty()) {
			return rq.historyBack("등록된 상품이 존재하지 않습니다.");
		}

		final ProductDetailDTO dto = ProductDetailDTO.of(optionalProduct.get());

		model.addAttribute(PRODUCT, dto);

		return "product/productDetail";
	}

	@PostMapping("/cartItems")
	@ResponseBody
	public RsData cartItems(Model model, @RequestBody Map<String, Object> data) {
		Map<Long, Long> quantities = data.values().stream()
			.map(row -> (Map<String, Object>) row)
			.collect(Collectors.toMap(
				map -> Long.parseLong(map.get("optionId").toString()),
				map -> Long.parseLong(map.get("cnt").toString()),
				(oldValue, newValue) -> oldValue
			));

		return RsData.of("S-1", "장바구니에 추가되었습니다.");
	}
}