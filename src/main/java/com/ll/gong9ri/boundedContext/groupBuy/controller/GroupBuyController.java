package com.ll.gong9ri.boundedContext.groupBuy.controller;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyDTO;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;
import com.ll.gong9ri.boundedContext.groupBuy.service.GroupBuyMemberService;
import com.ll.gong9ri.boundedContext.groupBuy.service.GroupBuyService;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.groupBuy.dto.DetailDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/groupBuy")
@RequiredArgsConstructor
public class GroupBuyController {
	private final GroupBuyService groupBuyService;
	private final ProductService productService;

	private final Rq rq;

	@PostMapping("/make/{productId}")
	@PreAuthorize("isAuthenticated()")
	public String createGroupBuy(@PathVariable("productId") Long productId) {
		boolean canCreateGroupBuy = productService.canCreateGroupBuy(productId);
		if (!canCreateGroupBuy) {
			return rq.historyBack("이미 공동구매가 존재합니다.");
		}

		Optional<Product> optionalProduct = productService.findById(productId);
		if (optionalProduct.isEmpty()) {
			return rq.historyBack("상품이 존재하지 않습니다.");
		}

		Member member = rq.getMember();

		RsData<GroupBuy> rsGroupBuy = groupBuyService.createGroupBuy(optionalProduct.get(), member);

		return rq.redirectWithMsg("/", rsGroupBuy);
	}

	@PostMapping("/{groupBuyId}/participate")
	@PreAuthorize("isAuthenticated()")
	public String participateGroupBuy(@PathVariable("groupBuyId") Long groupBuyId) {
		GroupBuy groupBuy = groupBuyService.findById(groupBuyId).orElse(null);
		Member member = rq.getMember();
		RsData<GroupBuy> rsGroupBuy = groupBuyService.save(groupBuy);
		return rq.redirectWithMsg("/groupBuy/details", rsGroupBuy);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/details")
	public String showDetails(Model model, DetailDTO detailDTO) {
		Optional<GroupBuy> opGroupBuy = groupBuyService.findById(detailDTO.getGroupBuyId());

		if (opGroupBuy.isEmpty()) {
			return rq.historyBack("존재하지 않는 공동구매 입니다.");
		}

		GroupBuyDTO groupBuyDTO = GroupBuyDTO.toDTO(opGroupBuy.get());

		model.addAttribute("groupBuy", groupBuyDTO);

		return "groupBuy/groupBuyDetails";
	}
}
