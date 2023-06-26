package com.ll.gong9ri.boundedContext.store.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.boundedContext.store.service.StoreService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {
	private final Rq rq;
	private StoreService storeService;

	@PreAuthorize("isAuthenticated() and hasAuthority('store')")
	@GetMapping("/me")
	public String storeMyPage() {
		return "usr/store/me";
	}
}
