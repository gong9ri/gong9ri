package com.ll.gong9ri.boundedContext.store.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.store.dto.StoreHomeDTO;
import com.ll.gong9ri.boundedContext.store.service.StoreService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/manage/store")
@RequiredArgsConstructor
public class ManageStoreController {
	private final Rq rq;
	private final StoreService storeService;

	@PreAuthorize("isAuthenticated() and hasAuthority('store')")
	@GetMapping("/")
	public String home(Model model) {
		RsData<StoreHomeDTO> rsStore = storeService.getStoreHome(rq.getMember().getId());

		model.addAttribute("store", rsStore.getData());

		return "usr/store/index";
	}
}
