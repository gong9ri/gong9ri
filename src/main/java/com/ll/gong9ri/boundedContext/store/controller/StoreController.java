package com.ll.gong9ri.boundedContext.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.store.dto.StoreHomeDTO;
import com.ll.gong9ri.boundedContext.store.service.StoreService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {
	private final Rq rq;
	private final StoreService storeService;

	@GetMapping("/{storeId}")
	public String home(Model model, @PathVariable Long storeId) {
		RsData<StoreHomeDTO> rsStore = storeService.getStoreHome(storeId);
		if (rsStore.isFail()) {
			return rq.historyBack(rsStore);
		}

		model.addAttribute("store", rsStore.getData());

		return "usr/store/index";
	}

	@GetMapping("/list")
	public String search(Model model, @RequestParam(defaultValue = "") String storeName) {
		model.addAttribute("stores", storeService.searchByName(storeName));

		return "usr/store/list";
	}
}
