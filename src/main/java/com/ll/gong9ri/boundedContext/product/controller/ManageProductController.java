package com.ll.gong9ri.boundedContext.product.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductDiscountDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductImageDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionNameDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductRegisterDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.service.ProductDiscountService;
import com.ll.gong9ri.boundedContext.product.service.ProductImageService;
import com.ll.gong9ri.boundedContext.product.service.ProductOptionService;
import com.ll.gong9ri.boundedContext.product.service.ProductService;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import com.ll.gong9ri.boundedContext.store.service.StoreService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_STORE')")
@RequestMapping("/manage/product")
@RequiredArgsConstructor
public class ManageProductController {
	private static final String PRODUCT = "product";
	private final ProductService productService;
	private final ProductOptionService optionService;
	private final ProductDiscountService discountService;
	private final ProductImageService imageService;
	private final StoreService storeService;
	private final Rq rq;

	/**
	 * @param productId
	 * @return check product is member's product
	 */
	private Boolean storeValidation(final Long productId) {
		// can be annotation?
		final Optional<Store> oStore = storeService.findByMemberId(rq.getMember().getId());
		if (oStore.isEmpty()) {
			return false;
		}

		return productService.storeValidation(productId, oStore.get().getId());
	}

	/**
	 * 판매자 상품 상세 페이지.
	 *
	 * OneToMany field 들을 Empty Collection 으로 보여줍니다.
	 * 판매자가 비어있는 항목들을 추가할 수 있도록 각 항목에 수정 버튼 으로 URL을 연결해야합니다.
	 *
	 * @param productId
	 * @param model
	 * @return ProductDTO
	 */
	@GetMapping("/{productId}/detail")
	public String detail(@PathVariable Long productId, Model model) {
		RsData<ProductDTO> rsProduct = productService.getProductDetail(productId);
		// TODO: rq.getMember`s store_id == rsProduct.getStore_id fail
		if (rsProduct.isFail()) {
			return rq.historyBack(rsProduct);
		}

		model.addAttribute(PRODUCT, rsProduct);

		return "product/detail";
	}

	@GetMapping("/registration")
	public String showProductRegistration() {
		return "product/productRegistration";
	}

	@PostMapping("/registration")
	public String registerProduct(@Valid ProductRegisterDTO productRegisterDTO) {
		RsData<Product> productRs = productService.registerProduct(productRegisterDTO);
		if (productRs.isFail()) {
			return rq.historyBack(productRs);
		}

		return rq.redirectWithMsg("/" + productRs.getData().getId() + "/detail", productRs);
	}

	@GetMapping("/{productId}/option")
	public String showProductOptionForm(@PathVariable Long productId, Model model) {
		List<ProductOptionNameDTO> options = optionService.getProductOptions(productId);

		model.addAttribute("options", options);

		return "product/optionDetails";
	}

	@PutMapping("/{productId}/option")
	public String addProductOptions(@PathVariable Long productId, @Valid ProductOptionDTO productOptionDTO) {
		RsData<Product> productRs = productService.addOptions(productId, productOptionDTO);
		if (productRs.isFail()) {
			return rq.historyBack("상품 상세 옵션 등록에 실했습니다.");
		}

		return rq.redirectWithMsg("/" + productRs.getData().getId() + "/detail", productRs);
	}

	@GetMapping("/{productId}/image")
	public String showProductImageForm(@PathVariable Long productId, Model model) {
		List<ProductImageDTO> images = imageService.getProductImages(productId);

		model.addAttribute("images", images);

		return "product/optionDetails";
	}

	@PutMapping("/{productId}/image")
	public String addProductImages(@PathVariable Long productId, @Valid List<ProductImageDTO> dtos) {
		RsData<Product> productRs = productService.addImages(productId, dtos);
		if (productRs.isFail()) {
			return rq.historyBack(productRs);
		}

		return rq.redirectWithMsg("/" + productRs.getData().getId() + "/detail", productRs);
	}

	@GetMapping("/{productId}/discount")
	public String showProductDiscountForm(@PathVariable Long productId, Model model) {
		List<ProductDiscountDTO> discounts = discountService.getProductDiscounts(productId);

		model.addAttribute("discounts", discounts);

		return "product/optionDetails";
	}

	@PutMapping("/{productId}/discount")
	public String addProductOptions(@PathVariable Long productId, @Valid List<ProductDiscountDTO> dtos) {
		RsData<Product> productRs = productService.addDiscounts(productId, dtos);
		if (productRs.isFail()) {
			return rq.historyBack("상품 할인 등록에 실했습니다.");
		}

		return rq.redirectWithMsg("/" + productRs.getData().getId() + "/detail", productRs);
	}
}