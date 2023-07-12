package com.ll.gong9ri.boundedContext.order.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ll.gong9ri.base.appConfig.AppConfig;
import com.ll.gong9ri.base.tosspayments.tossConfig.TossConfig;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.base.tosspayments.service.PaymentService;
import com.ll.gong9ri.boundedContext.order.dto.OrderInfoListDTO;
import com.ll.gong9ri.boundedContext.order.dto.OrderRecipientDTO;
import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;
import com.ll.gong9ri.boundedContext.order.entity.OrderLog;
import com.ll.gong9ri.boundedContext.order.entity.OrderStatus;
import com.ll.gong9ri.boundedContext.order.entity.ProductOptionQuantity;
import com.ll.gong9ri.boundedContext.order.service.OrderInfoService;
import com.ll.gong9ri.boundedContext.order.service.OrderLogService;
import com.ll.gong9ri.boundedContext.product.dto.ProductGroupBuyDetailDTO;
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
		if (oOrderInfo.isEmpty() || !oOrderInfo.get().getMember().getId().equals(rq.getMember().getId())) {
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
		final Optional<OrderLog> oOrderLog = orderLogService.findById(rsOrder.getData().getRecentOrderLogId());
		oOrderLog.ifPresent(orderLog -> model.addAttribute("order", orderLog));

		return "usr/order/detail";
	}

	@GetMapping("/choose/{orderId}")
	public String chooseForm(Model model, @PathVariable Long orderId) {
		final RsData<OrderInfo> rsOrder = orderValidate(orderId);
		if (rsOrder.isFail() || rsOrder.getData().getOrderStatus() != OrderStatus.GROUP_BUY_CREATED) {
			return rq.historyBack(rsOrder);
		}

		final ProductGroupBuyDetailDTO dto = ProductGroupBuyDetailDTO.of(rsOrder.getData());
		model.addAttribute("product", dto);
		model.addAttribute("orderId", orderId);

		return "usr/order/choose";
	}

	@PutMapping("/choose/{orderId}")
	@ResponseBody
	public RsData<Long> chooseOptionOrder(
		@PathVariable Long orderId,
		@RequestBody Map<String, List<ProductOptionQuantity>> choices
	) {
		final RsData<OrderInfo> rsOrder = orderValidate(orderId);
		if (rsOrder.isFail()) {
			return RsData.of("F-1", FORBIDDEN_MESSAGE, null);
		}

		final RsData<OrderInfo> rsCreateOrderInfo = orderInfoService.groupBuyConfirm(
			rsOrder.getData(),
			choices.get("choices")
		);

		if (rsCreateOrderInfo.isFail()) {
			return RsData.of("F-2", FORBIDDEN_MESSAGE, null);
		}

		return RsData.successOf(rsCreateOrderInfo.getData().getId());
	}

	@PostMapping("/create/{productId}")
	@ResponseBody
	public RsData<Long> createOrder(
		@PathVariable Long productId,
		@RequestBody Map<String, List<ProductOptionQuantity>> choices
	) {
		final Optional<Product> oProduct = productService.getProduct(productId);
		if (oProduct.isEmpty()) {
			return RsData.of("F-1", "존재하지 않는 상품입니다.", null);
		}

		final RsData<OrderInfo> rsCreateOrderInfo = orderInfoService.create(
			rq.getMember(),
			oProduct.get(),
			choices.get("choices")
		);

		if (rsCreateOrderInfo.isFail()) {
			return RsData.of("F-2", FORBIDDEN_MESSAGE, null);
		}

		return RsData.successOf(rsCreateOrderInfo.getData().getId());
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

		return "usr/order/recipientForm";
	}

	@PutMapping("/confirm/{orderId}")
	public String confirmOrder(@PathVariable Long orderId, @Valid OrderRecipientDTO orderRecipientDTO) {
		final RsData<OrderInfo> rsOrderInfo = orderValidate(orderId);
		if (rsOrderInfo.isFail()) {
			return rq.historyBack(rsOrderInfo);
		}

		final RsData<OrderInfo> rsConfirmOrder = orderInfoService.confirm(
			rsOrderInfo.getData(),
			orderRecipientDTO
		);

		return rq.redirectWithMsg("/order/detail/" + rsConfirmOrder.getData().getId(), rsConfirmOrder);
	}

	@GetMapping("/payment/{orderId}")
	public String paymentForm(@PathVariable Long orderId, Model model) {
		final RsData<OrderInfo> rsOrderInfo = orderValidate(orderId);
		if (rsOrderInfo.isFail()) {
			return rq.historyBack(rsOrderInfo);
		}

		final Optional<OrderLog> oOrderLog = orderLogService.findById(rsOrderInfo.getData().getRecentOrderLogId());
		if (oOrderLog.isEmpty()) {
			return rq.historyBack(FORBIDDEN_MESSAGE);
		}

		model.addAttribute("clientKey", TossConfig.getCLIENT_KEY());
		model.addAttribute("successUrl", AppConfig.getBaseUrl() + "/payment/" + orderId + "/success");
		model.addAttribute("failUrl", AppConfig.getBaseUrl() + "/payment/" + orderId + "/fail");
		model.addAttribute("orderLog", oOrderLog.get());
		return "usr/order/payment";
	}

	@GetMapping("/payment/{orderId}/success")
	public String paymentSuccess(@PathVariable Long orderId, Model model) {
		final RsData<OrderInfo> rsOrderInfo = orderValidate(orderId);
		if (rsOrderInfo.isFail()) {
			return rq.historyBack(rsOrderInfo);
		}

		final Optional<OrderLog> oOrderLog = orderLogService.findById(rsOrderInfo.getData().getRecentOrderLogId());
		if (oOrderLog.isEmpty()) {
			return rq.historyBack(FORBIDDEN_MESSAGE);
		}

		return "usr/order/payment";
	}

	@GetMapping("/payment/{orderId}/fail")
	public String paymentFail(@PathVariable Long orderId, Model model) {
		final RsData<OrderInfo> rsOrderInfo = orderValidate(orderId);
		if (rsOrderInfo.isFail()) {
			return rq.historyBack(rsOrderInfo);
		}

		final Optional<OrderLog> oOrderLog = orderLogService.findById(rsOrderInfo.getData().getRecentOrderLogId());
		if (oOrderLog.isEmpty()) {
			return rq.historyBack(FORBIDDEN_MESSAGE);
		}

		return "usr/order/payment";
	}
}