package com.ll.gong9ri.boundedContext.store.controller;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.service.ProductService;
import com.ll.gong9ri.boundedContext.store.dto.StoreHomeDTO;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import com.ll.gong9ri.boundedContext.store.service.StoreService;

import lombok.RequiredArgsConstructor;

@Controller
@PreAuthorize("isAuthenticated() and hasAuthority('ROLE_STORE')")
@RequestMapping("/manage/store")
@RequiredArgsConstructor
public class ManageStoreController {
	private static final String DEFAULT_ERROR_MESSAGE = "관리자에게 문의하세요.";
	private final Rq rq;
	private final StoreService storeService;
	private final ProductService productService;

	@GetMapping("/")
	public String home(Model model) {
		final Optional<Store> oStore = storeService.findByMemberId(rq.getMember().getId());
		if (oStore.isEmpty()) {
			return rq.redirectWithErrorMsg(DEFAULT_ERROR_MESSAGE, "/");
		}

		RsData<StoreHomeDTO> rsStore = storeService.getStoreHome(oStore.get().getId());

		model.addAttribute("store", rsStore.getData());

		return "usr/manage/store/index";
	}

	@GetMapping("/product/list")
	public String list(Model model, @RequestParam String kw) {
		// TODO: getProductsByStore
		// model.addAttribute("stores", getStore());

		return "usr/manage/store/list";
	}
}
