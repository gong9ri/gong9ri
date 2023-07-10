package com.ll.gong9ri.boundedContext.store.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;
import com.ll.gong9ri.boundedContext.order.service.OrderInfoService;
import com.ll.gong9ri.boundedContext.product.entity.Product;
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
	private final OrderInfoService orderInfoService;

	@GetMapping("/")
	public String home(Model model) {
		final Optional<Store> oStore = storeService.findByMemberId(rq.getMember().getId());
		if (oStore.isEmpty()) {
			return rq.redirectWithErrorMsg(DEFAULT_ERROR_MESSAGE, "/");
		}

		final RsData<StoreHomeDTO> rsStore = storeService.getStoreHome(oStore.get().getId());
		final List<Product> products = productService.getAllProductsByStore(oStore.get().getId());

		model.addAttribute("store", rsStore.getData());
		model.addAttribute("products", products); // TODO: dto

		return "manage/store/index";
	}

	@GetMapping("/order")
	public String orderList(Model model) {
		final Optional<Store> oStore = storeService.findByMemberId(rq.getMember().getId());
		if (oStore.isEmpty()) {
			return rq.redirectWithErrorMsg(DEFAULT_ERROR_MESSAGE, "/");
		}

		model.addAttribute("orders", orderInfoService.findByStoreId(oStore.get().getId()));

		return "manage/store/order";
	}

	@GetMapping("/order/detail/{orderId}")
	public String orderList(@PathVariable Long orderId, Model model) {
		final Optional<Store> oStore = storeService.findByMemberId(rq.getMember().getId());
		if (oStore.isEmpty()) {
			return rq.redirectWithErrorMsg(DEFAULT_ERROR_MESSAGE, "/");
		}

		final Optional<OrderInfo> oOrderInfo = orderInfoService.findById(orderId);
		if (oOrderInfo.isEmpty()) {
			return rq.redirectWithErrorMsg(DEFAULT_ERROR_MESSAGE, "/");
		}

		model.addAttribute("order", oOrderInfo.get());

		return "manage/store/order";
	}
}
