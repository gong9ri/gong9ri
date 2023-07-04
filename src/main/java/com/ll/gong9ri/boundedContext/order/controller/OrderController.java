package com.ll.gong9ri.boundedContext.order.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;
import com.ll.gong9ri.boundedContext.order.service.OrderService;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;
	private final Rq rq;

	/*
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String showOrderForm(Model model) {
		// TODO: getProductOptions
		model.addAttribute("productOptions");
		return "usr/order/orderForm";
	}
	*/

	/*
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String createOrder() {

		return rq.redirectWithMsg("/order/detail/" + orderRs.getData().getId(), orderRs.getMsg());
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/detail/{orderId}")
	public String orderDetail(@PathVariable Long orderId) {

		return rq.redirectWithMsg("/order", orderRs.getMsg());
	}

	 */

	@PreAuthorize("isAuthenticated()")
	@PutMapping("/confirm/{orderId}")
	public String confirmOrder(@PathVariable Long orderId) {
		Long productId = 0L;
		Product product = null;
		Map<ProductOption, Integer> productOptions = new HashMap<>();
		RsData<OrderInfo> rsOrderInfo = orderService.confirmOrder(rq.getMember().getId(), orderId, productOptions);
		return rq.redirectWithMsg("/order", rsOrderInfo);
	}
}


