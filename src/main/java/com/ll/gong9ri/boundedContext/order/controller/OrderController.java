package com.ll.gong9ri.boundedContext.order.controller;

import java.util.List;
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
import com.ll.gong9ri.boundedContext.order.dto.OrderInfoListDTO;
import com.ll.gong9ri.boundedContext.order.dto.OrderRecipientDTO;
import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;
import com.ll.gong9ri.boundedContext.order.entity.OrderLog;
import com.ll.gong9ri.boundedContext.order.entity.OrderStatus;
import com.ll.gong9ri.boundedContext.order.service.OrderInfoService;
import com.ll.gong9ri.boundedContext.order.service.OrderLogService;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
	private static final String FORBIDDEN_MESSAGE = "잘못된 접근입니다.";
	private final OrderInfoService orderInfoService;
	private final OrderLogService orderLogService;
	private final PaymentService paymentService;
	private final ProductService productService;
	private final Rq rq;

	private RsData<OrderInfo> orderValidate(final Long orderId) {
		final Optional<OrderInfo> oOrderInfo = orderInfoService.findById(orderId);
		if (oOrderInfo.isEmpty() || oOrderInfo.get().getMember().getId().equals(rq.getMember().getId())) {
			return RsData.of("F-1", FORBIDDEN_MESSAGE, null);
		}

		return RsData.successOf(oOrderInfo.get());
	}

	@GetMapping("/list")
	public String list(Model model) {
		final List<OrderInfoListDTO> orderInfos = orderInfoService.findByMemberId(rq.getMember().getId())
			.stream()
			.map(OrderInfoListDTO::of)
			.toList();

		model.addAttribute("orders", orderInfos);

		return "usr/order/list";
	}

	@GetMapping("/detail/{orderId}")
	public String getDetail(@PathVariable Long orderId, Model model) {
		final RsData<OrderInfo> rsOrder = orderValidate(orderId);
		if (rsOrder.isFail()) {
			return rq.historyBack(rsOrder);
		}

		final OrderStatus currentStatus = rsOrder.getData().getOrderStatus();
		model.addAttribute("orderId", orderId);
		model.addAttribute("currentStatus", currentStatus);

		if (currentStatus == OrderStatus.PRE_CREATED || currentStatus == OrderStatus.GROUP_BUY_CREATED) {
			model.addAttribute("confirmOrder", "/confirm/" + orderId);
		}

		if (currentStatus == OrderStatus.OPTION_SELECTED) {
			model.addAttribute("createPayment", "/create/payment/" + orderId);
		}

		final Optional<OrderLog> oOrderLog = orderLogService.findById(rsOrder.getData().getRecentOrderLogId());
		oOrderLog.ifPresent(orderLog -> model.addAttribute("order", orderLog));

		return "usr/order/detail";
	}

	@PostMapping("/create/{productId}")
	public String createOrder(@PathVariable Long productId) {
		final Optional<Product> oProduct = productService.getProduct(productId);
		if (oProduct.isEmpty()) {
			return rq.historyBack(FORBIDDEN_MESSAGE);
		}

		final RsData<OrderInfo> rsOrderInfo = orderInfoService.preCreate(rq.getMember(), oProduct.get());
		if (rsOrderInfo.isFail()) {
			return rq.historyBack(FORBIDDEN_MESSAGE);
		}

		final RsData<OrderInfo> rsCreateOrderInfo = orderInfoService.create(rsOrderInfo.getData());
		if (rsCreateOrderInfo.isFail()) {
			return rq.historyBack(FORBIDDEN_MESSAGE);
		}

		return rq.redirectWithMsg(
			"/order/detail/" + rsCreateOrderInfo.getData().getId(),
			"주문이 성공적으로 만들어졌습니다."
		);
	}

	@GetMapping("/confirm/{orderId}")
	public String confirmOrderForm(@PathVariable Long orderId, Model model) {
		final RsData<OrderInfo> rsOrder = orderValidate(orderId);
		if (rsOrder.isFail()) {
			return rq.historyBack(rsOrder);
		}

		final OrderInfo orderInfo = rsOrder.getData();
		model.addAttribute("orderDetail", orderInfo);
		model.addAttribute("productDetail", productService.getProductDetail(orderInfo.getProduct().getId()));

		return "usr/order/orderProductOption";
	}

	@PutMapping("/confirm/{orderId}")
	public String confirmOrder(@PathVariable Long orderId, @Valid OrderCreateDTO orderCreateDTO) {
		RsData<OrderInfo> rsOrderInfo = orderValidate(orderId);
		if (rsOrderInfo.isFail()) {
			return rq.historyBack(rsOrderInfo);
		}

		final RsData<OrderInfo> rsConfirmOrderInfo = orderInfoService.confirm(
			rsOrderInfo.getData(),
			OrderRecipientDTO.builder()
				.recipient(orderCreateDTO.getRecipient())
				.mainAddress(orderCreateDTO.getMainAddress())
				.subAddress(orderCreateDTO.getSubAddress())
				.build(),
			orderCreateDTO.getOptions()
		);

		return rq.redirectWithMsg(
			"/order/detail/" + rsConfirmOrderInfo.getData().getId(),
			"주문이 성공적으로 결정됐습니다."
		);
	}

	@PostMapping("/create/payment/{orderId}")
	public String createPayment(@PathVariable Long orderId, Model model) {
		final RsData<OrderInfo> rsOrderInfo = orderValidate(orderId);
		if (rsOrderInfo.isFail()) {
			return rq.historyBack(rsOrderInfo);
		}

		final Optional<OrderLog> oOrderLog = orderLogService.findById(rsOrderInfo.getData().getRecentOrderLogId());
		if (oOrderLog.isEmpty()) {
			return rq.historyBack(FORBIDDEN_MESSAGE);
		}

		// TODO: purchase redirect

		final RsData<PaymentResult> paymentResult = paymentService.createPayment(oOrderLog.get());
		if (paymentResult.isFail()) {
			return rq.historyBack(paymentResult);
		}

		model.addAttribute("paymentResult", paymentResult.getData().getPaymentKey());

		return "order/payment";
	}
}