package com.ll.gong9ri.boundedContext.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.base.tosspayments.entity.PaymentResult;
import com.ll.gong9ri.base.tosspayments.service.PaymentService;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;
import com.ll.gong9ri.boundedContext.order.service.OrderService;
import com.ll.gong9ri.boundedContext.order.service.ProductOptionQuantityService;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;
import com.ll.gong9ri.boundedContext.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;
	private final ProductOptionQuantityService productOptionQuantityService;
	private final Rq rq;
	private final PaymentService paymentService;
	private final Product product;
	private final Member member;
	private final OrderInfo orderInfo;
	private final ProductService productService;


	@GetMapping("/create")
	public String createOrder(Long productId, Model model) {
		// Product product = productService.findProductById(productId);

		RsData<OrderInfo> rsCreateOrderInfo = orderService.createOrder(member, product);
		List<String> optionOneDetails = product.getProductOptions().stream()
			.map(ProductOption::getOptionOneName)
			.collect(Collectors.toList());

		List<String> optionTwoDetails = product.getProductOptions().stream()
			.map(ProductOption::getOptionTwoName)
			.collect(Collectors.toList());

		ProductOptionDTO productOptionDTO = ProductOptionDTO.builder()
			.optionOneDetails(optionOneDetails)
			.optionTwoDetails(optionTwoDetails)
			.build();
		model.addAttribute("productOptionDTO", productOptionDTO);

		// 주문 정보를 모델에 저장
		model.addAttribute("productId", productId);
		model.addAttribute("orderInfo", rsCreateOrderInfo.getData());

		return "usr/order/orderProductOption";
	}

	@PostMapping("/confirm")
	public String confirmOrder(Long orderId, Model model) {
		final Map<ProductOption, Integer> options = new HashMap<>();
		options.put(product.getProductOptions().get(1), 3);
		options.put(product.getProductOptions().get(4), 1);
		options.put(product.getProductOptions().get(5), 2);

		RsData<OrderInfo> rsConfirmOrderInfo = orderService.confirmOrder(member.getId(), orderId, options);
		model.addAttribute("orderInfo", rsConfirmOrderInfo.getData());

		return "usr/order/orderProductOption";
	}

	@PostMapping("/createPayment")
	public String createPayment(OrderInfo orderInfo, Model model) {
		RsData<PaymentResult> paymentResult = paymentService.createPayment(orderInfo);
		// PaymentResult paymentResult = paymentService.createPayment(orderInfo).getData();

		model.addAttribute("paymentKey", paymentResult.getData().getPaymentKey());

		return "order/payment";
	}
}