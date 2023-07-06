package com.ll.gong9ri.boundedContext.order.controller;

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
import com.ll.gong9ri.base.tosspayments.entity.PaymentResult;
import com.ll.gong9ri.base.tosspayments.service.PaymentService;
import com.ll.gong9ri.boundedContext.order.dto.OrderCreateDTO;
import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;
import com.ll.gong9ri.boundedContext.order.service.OrderService;
import com.ll.gong9ri.boundedContext.order.service.ProductOptionQuantityService;
import com.ll.gong9ri.boundedContext.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;
	private final ProductOptionQuantityService productOptionQuantityService;
	private final PaymentService paymentService;
	private final ProductService productService;
	private final Rq rq;

	private RsData<OrderInfo> orderValidate(final Long orderId) {
		final Optional<OrderInfo> oOrderInfo = orderService.findById(orderId);
		if (oOrderInfo.isEmpty() || oOrderInfo.get().getMemberId().equals(rq.getMember().getId())) {
			return RsData.of("F-1", "잘못된 접근입니다.", null);
		}

		return RsData.successOf(oOrderInfo.get());
	}

	@GetMapping("/detail/{orderId}")
	public String getDetail(@PathVariable Long orderId, Model model) {
		RsData<OrderInfo> rsOrder = orderValidate(orderId);

		// model.addAttribute("orderInfo", oOrderInfo.get()); // DTO 변환 필요함
		return "usr/order/detail";
	}

	@PostMapping("/create/{productId}")
	public String createOrder(@PathVariable Long productId, Model model) {
		// RsData<?> rsProduct = productService.findById(productId);
		// OrderInfo orderInfo = orderService.createOrder(rq.getMember(), rsProduct.getData());

		return rq.redirectWithMsg("/order/detail/" + "orderInfo.getId()", "주문이 성공적으로 만들어졌습니다."); // 따옴표 지우기
	}

	@GetMapping("/confirm/{orderId}")
	public String confirmOrderForm(@PathVariable Long orderId, Model model) {
		RsData<OrderInfo> rsOrder = orderValidate(orderId);

		// final OrderInfo orderInfo = oOrderInfo.get();

		// Product product = productService.findProductById(orderinfo().getProductId());

		// RsData<OrderInfo> rsCreateOrderInfo = orderService.createOrder(member, product);
		// List<String> optionOneDetails = product.getProductOptions().stream()
		// 	.map(ProductOption::getOptionOneName)
		// 	.collect(Collectors.toList());
		//
		// List<String> optionTwoDetails = product.getProductOptions().stream()
		// 	.map(ProductOption::getOptionTwoName)
		// 	.collect(Collectors.toList());

		// ProductOptionDTO productOptionDTO = ProductOptionDTO.builder()
		// 	.optionOneDetails(optionOneDetails)
		// 	.optionTwoDetails(optionTwoDetails)
		// 	.build();
		// model.addAttribute("productOptionDTO", productOptionDTO);

		// 주문 정보를 모델에 저장
		// model.addAttribute("productId", product);
		// model.addAttribute("orderInfo", rsCreateOrderInfo.getData());

		return "usr/order/orderProductOption";
	}

	@PutMapping("/confirm/{orderId}")
	public String confirmOrder(@PathVariable Long orderId, @Valid OrderCreateDTO orderCreateDTO, Model model) {
		RsData<OrderInfo> rsOrder = orderValidate(orderId);

		// final OrderInfo orderInfo = oOrderInfo.get();

		// RsData<OrderInfo> rsConfirmOrderInfo = orderService.confirmOrder(rq.getMember().getId(), orderId, options);
		// model.addAttribute("orderInfo", rsConfirmOrderInfo.getData());

		return "usr/order/orderProductOption";
	}

	@PostMapping("/createPayment/{orderId}")
	public String createPayment(@PathVariable Long orderId, Model model) {
		RsData<OrderInfo> rsOrder = orderValidate(orderId);

		RsData<PaymentResult> paymentResult = paymentService.createPayment(rsOrder.getData());
		// PaymentResult paymentResult = paymentService.createPayment(orderInfo).getData();

		model.addAttribute("paymentKey", paymentResult.getData().getPaymentKey());

		return "order/payment";
	}
}