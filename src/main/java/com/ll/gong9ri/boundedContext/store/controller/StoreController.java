package com.ll.gong9ri.boundedContext.store.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.product.service.ProductService;
import com.ll.gong9ri.boundedContext.store.dto.StoreHomeDTO;
import com.ll.gong9ri.boundedContext.store.dto.StoreListDTO;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import com.ll.gong9ri.boundedContext.store.service.StoreService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {
	private final Rq rq;
	private final StoreService storeService;
	private final ProductService productService;

	@GetMapping("/{storeId}")
	public String detail(Model model, @PathVariable Long storeId) {
		final Optional<Store> oStore = storeService.findById(storeId);
		if (oStore.isEmpty()) {
			return rq.historyBack("잘못된 접근입니다.");
		}

		final StoreHomeDTO storeHomeDTO = StoreHomeDTO.builder()
			.storeId(oStore.get().getId())
			.storeName(oStore.get().getName())
			.products(productService.getProductsByStoreId(storeId)
				.getData()
				.stream()
				.map(ProductDTO::toDTO)
				.toList())
			.build();

		model.addAttribute("store", storeHomeDTO);

		return "usr/store/index";
	}

	@GetMapping("/list")
	public String search(Model model, @RequestParam(defaultValue = "") String storeName) {
		final List<StoreListDTO> stores = storeService.searchByName(storeName)
			.stream()
			.map(e -> StoreListDTO.builder()
				.storeId(e.getId())
				.storeName(e.getName())
				.productCount(productService.getCountByStore(e.getId()))
				.build())
			.toList();

		model.addAttribute("stores", stores);

		return "usr/store/list";
	}
}
