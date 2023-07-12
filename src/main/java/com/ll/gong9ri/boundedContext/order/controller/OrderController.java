package com.ll.gong9ri.boundedContext.order.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ll.gong9ri.base.appConfig.AppConfig;
import com.ll.gong9ri.base.tosspayments.tossConfig.TossConfig;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

	private RsData<OrderInfo> orderValidate(final Long id) {
		final Optional<OrderInfo> oOrderInfo = orderInfoService.findById(id);
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

	@GetMapping("/detail/{id}")
	public String getDetail(@PathVariable Long id, Model model) {
		final RsData<OrderInfo> rsOrder = orderValidate(id);
		if (rsOrder.isFail()) {
			return rq.historyBack(rsOrder);
		}

		final OrderStatus currentStatus = rsOrder.getData().getOrderStatus();
		model.addAttribute("orderId", id);
		model.addAttribute("currentStatus", currentStatus);
		final Optional<OrderLog> oOrderLog = orderLogService.findById(rsOrder.getData().getRecentOrderLogId());
		oOrderLog.ifPresent(orderLog -> model.addAttribute("order", orderLog));

		return "usr/order/detail";
	}

	@GetMapping("/choose/{id}")
	public String chooseForm(Model model, @PathVariable Long id) {
		final RsData<OrderInfo> rsOrder = orderValidate(id);
		if (rsOrder.isFail() || rsOrder.getData().getOrderStatus() != OrderStatus.GROUP_BUY_CREATED) {
			return rq.historyBack(rsOrder);
		}

		final ProductGroupBuyDetailDTO dto = ProductGroupBuyDetailDTO.of(rsOrder.getData());
		model.addAttribute("product", dto);
		model.addAttribute("orderId", id);

		return "usr/order/choose";
	}

	@PutMapping("/choose/{id}")
	@ResponseBody
	public RsData<Long> chooseOptionOrder(
		@PathVariable Long id,
		@RequestBody Map<String, List<ProductOptionQuantity>> choices
	) {
		final RsData<OrderInfo> rsOrder = orderValidate(id);
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

	@GetMapping("/confirm/{id}")
	public String confirmOrderForm(@PathVariable Long id, Model model) {
		final RsData<OrderInfo> rsOrder = orderValidate(id);
		if (rsOrder.isFail()) {
			return rq.historyBack(rsOrder);
		}

		final OrderInfo orderInfo = rsOrder.getData();
		model.addAttribute("orderDetail", orderInfo);
		model.addAttribute("productDetail", productService.getProductDetail(orderInfo.getProduct().getId()));

		return "usr/order/recipientForm";
	}

	@PutMapping("/confirm/{id}")
	public String confirmOrder(@PathVariable Long id, @Valid OrderRecipientDTO orderRecipientDTO) {
		final RsData<OrderInfo> rsOrderInfo = orderValidate(id);
		if (rsOrderInfo.isFail()) {
			return rq.historyBack(rsOrderInfo);
		}

		final RsData<OrderInfo> rsConfirmOrder = orderInfoService.confirm(
			rsOrderInfo.getData(),
			orderRecipientDTO
		);

		return rq.redirectWithMsg("/order/detail/" + rsConfirmOrder.getData().getId(), rsConfirmOrder);
	}

	@GetMapping("/payment/{id}")
	public String paymentForm(@PathVariable Long id, Model model) {
		final RsData<OrderInfo> rsOrderInfo = orderValidate(id);
		if (rsOrderInfo.isFail()) {
			return rq.historyBack(rsOrderInfo);
		}

		final Optional<OrderLog> oOrderLog = orderLogService.findById(rsOrderInfo.getData().getRecentOrderLogId());
		if (oOrderLog.isEmpty()) {
			return rq.historyBack(FORBIDDEN_MESSAGE);
		}

		model.addAttribute("clientKey", TossConfig.getCLIENT_KEY());
		model.addAttribute("successUrl", AppConfig.getBaseUrl() + "/order/payment/" + id + "/success");
		model.addAttribute("failUrl", AppConfig.getBaseUrl() + "/order/payment/" + id + "/fail");
		model.addAttribute("orderLog", oOrderLog.get());
		return "usr/order/payment";
	}

	@GetMapping("/payment/{id}/success")
	public String paymentSuccess(
			@PathVariable Long id,
			@RequestParam(value = "orderId") String orderId,
			@RequestParam(value = "amount") Integer amount,
			@RequestParam(value = "paymentKey") String paymentKey
	) throws RuntimeException {
		final RsData<OrderInfo> rsOrderInfo = orderValidate(id);
		if (rsOrderInfo.isFail()) {
			return rq.historyBack(rsOrderInfo);
		}

		final Optional<OrderLog> oOrderLog = orderLogService.findById(rsOrderInfo.getData().getRecentOrderLogId());
		if (oOrderLog.isEmpty()) {
			return rq.historyBack(FORBIDDEN_MESSAGE);
		}

		final OrderLog orderLog = oOrderLog.get();
		if (!orderLog.getOrderId().equals(orderId) || !orderLog.getTotalPrice().equals(amount)) {
			throw new RuntimeException("결제 정보가 변조되었습니다.");
		}

		final RsData<OrderInfo> rsPayment = orderInfoService.payment(rsOrderInfo.getData(), paymentKey);
		if (rsPayment.isFail()) {
			return rq.historyBack(rsPayment);
		}

		final Optional<OrderLog> paymentOrderLog = orderLogService.findById(rsPayment.getData().getRecentOrderLogId());
		if (paymentOrderLog.isEmpty()) {
			return rq.historyBack(FORBIDDEN_MESSAGE);
		}

		RsData<Boolean> result = paymentService.confirmPayment(paymentOrderLog.get());
		if (result.isFail()) {
			return rq.historyBack(result);
		}

		return rq.redirectWithMsg("/order/detail/" + rsPayment.getData().getId(), rsPayment);
	}

	@GetMapping("/payment/{id}/fail")
	public String paymentFail(@PathVariable Long id) {
		return rq.redirectWithErrorMsg("/order/detail/" + id, "결제에 실패했습니다.");
	}
}