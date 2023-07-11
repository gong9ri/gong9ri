package com.ll.gong9ri.boundedContext.product.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductDiscountDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductImageDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDetailDTO;
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

		model.addAttribute(PRODUCT, rsProduct.getData());

		return "product/detail";
	}

	@GetMapping("/registration")
	public String showProductRegistration() {
		return "product/productRegistration";
	}

	@PostMapping("/registration")
	public String registerProduct(@Valid ProductRegisterDTO productRegisterDTO) {
		final Optional<Store> oStore = storeService.findByMemberId(rq.getMember().getId());
		if (oStore.isEmpty()) {
			return rq.historyBack("잘못된 접근입니다.");
		}

		RsData<Product> productRs = productService.registerProduct(oStore.get(), productRegisterDTO);
		if (productRs.isFail()) {
			return rq.historyBack(productRs);
		}

		return rq.redirectWithMsg("/manage/product/" + productRs.getData().getId() + "/detail", productRs);
	}

	@GetMapping("/{productId}/option")
	public String showProductOptionForm(@PathVariable Long productId, Model model) {
		List<ProductOptionDetailDTO> options = optionService.getProductOptions(productId);

		model.addAttribute("options", options);

		return "product/optionDetail";
	}

	@PutMapping("/{productId}/option")
	@ResponseBody
	public ResponseEntity<Long> addProductOptions(@PathVariable Long productId, @RequestBody @Valid ProductOptionDTO dto) {
		RsData<Product> productRs = productService.addOptions(productId, dto);

		if (productRs.isFail()) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok().body(productRs.getData().getId());
	}

	@GetMapping("/{productId}/image")
	public String showProductImageForm(@PathVariable Long productId, Model model) {
		List<ProductImageDTO> images = imageService.getProductImages(productId);

		model.addAttribute("images", images);

		return "product/image";
	}

	@PutMapping("/{productId}/image")
	public String addProductImages(@PathVariable Long productId, @Valid List<ProductImageDTO> dtos) {
		RsData<Product> productRs = productService.addImages(productId, dtos);
		if (productRs.isFail()) {
			return rq.historyBack(productRs);
		}

		return rq.redirectWithMsg("/manage/product/%d/detail".formatted(productRs.getData().getId()), productRs);
	}

	@GetMapping("/{productId}/discount")
	public String showProductDiscountForm(@PathVariable Long productId, Model model) {
		List<ProductDiscountDTO> discounts = discountService.getProductDiscounts(productId);

		model.addAttribute("discounts", discounts);

		return "product/discount";
	}

	@PutMapping("/{productId}/discount")
	public String addProductDiscounts(@PathVariable Long productId, @Valid List<ProductDiscountDTO> dtos) {
		RsData<Product> productRs = productService.addDiscounts(productId, dtos);
		if (productRs.isFail()) {
			return rq.historyBack("상품 할인 등록에 실패했습니다.");
		}

		return rq.redirectWithMsg("/manage/product/%d/detail".formatted(productRs.getData().getId()), productRs);
	}
}
