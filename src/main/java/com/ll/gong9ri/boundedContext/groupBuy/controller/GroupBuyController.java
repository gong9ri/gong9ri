package com.ll.gong9ri.boundedContext.groupBuy.controller;

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
import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyDTO;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyMember;
import com.ll.gong9ri.boundedContext.groupBuy.service.GroupBuyMemberService;
import com.ll.gong9ri.boundedContext.groupBuy.service.GroupBuyService;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/groupBuy")
@RequiredArgsConstructor
public class GroupBuyController {
	private final GroupBuyService groupBuyService;
	private final ProductService productService;
	private final GroupBuyMemberService groupBuyMemberService;
	private final Rq rq;

	private RsData<GroupBuy> validateGroupBuy(final Long groupBuyId) {
		final Optional<GroupBuy> oGroupBuy = groupBuyService.getProgressGroupBuy(groupBuyId);
		return oGroupBuy.map(RsData::successOf)
			.orElseGet(() -> RsData.of("F-1", "잘못된 접근입니다.", null));
	}

	@PostMapping("/make/{productId}")
	@PreAuthorize("isAuthenticated()")
	public String createGroupBuy(@PathVariable("productId") Long productId) {
		final Optional<Product> optionalProduct = productService.findById(productId);
		if (optionalProduct.isEmpty()) {
			return rq.historyBack("상품이 존재하지 않습니다.");
		}

		final RsData<GroupBuy> rsGroupBuy = groupBuyService.create(optionalProduct.get());
		groupBuyMemberService.addLeader(rq.getMember(), rsGroupBuy.getData());
		groupBuyMemberService.addStore(optionalProduct.get().getStore().getMember(), rsGroupBuy.getData());

		return rq.redirectWithMsg("/groupBuy/list", rsGroupBuy);
	}

	@GetMapping("/list")
	public String groupBuyList(Model model) {
		// TODO: search option
		final List<GroupBuyDTO> groupBuyDTOs = groupBuyService.findAllByDTO();
		model.addAttribute("groupBuyList", groupBuyDTOs);

		return "groupBuy/list";
	}

	@PostMapping("/{groupBuyId}/participate")
	@PreAuthorize("isAuthenticated()")
	public String participateGroupBuy(@PathVariable("groupBuyId") Long groupBuyId) {
		final RsData<GroupBuy> rsGroupBuy = validateGroupBuy(groupBuyId);
		if (rsGroupBuy.isFail()) {
			return rq.historyBack(rsGroupBuy);
		}

		final RsData<GroupBuyMember> rsGroupBuyMember = groupBuyMemberService.addGeneral(
			rq.getMember(),
			rsGroupBuy.getData()
		);
		if (rsGroupBuyMember.isFail()) {
			return rq.historyBack(rsGroupBuyMember);
		}

		return rq.redirectWithMsg("/groupBuy/detail/" + groupBuyId, rsGroupBuyMember);
	}

	@PutMapping("/{groupBuyId}/cancel")
	@PreAuthorize("isAuthenticated()")
	public String participateCancelGroupBuy(@PathVariable("groupBuyId") Long groupBuyId) {
		final RsData<GroupBuy> rsGroupBuy = validateGroupBuy(groupBuyId);
		if (rsGroupBuy.isFail()) {
			return rq.historyBack(rsGroupBuy);
		}

		final RsData<Void> rsGroupBuyMember = groupBuyMemberService.delete(
			rsGroupBuy.getData(),
			rq.getMember()
		);
		if (rsGroupBuyMember.isFail()) {
			return rq.historyBack(rsGroupBuyMember);
		}

		return rq.redirectWithMsg("/groupBuy/detail/" + groupBuyId, rsGroupBuyMember);
	}

	@GetMapping("/detail/{groupBuyId}")
	public String showDetail(Model model, @PathVariable("groupBuyId") Long groupBuyId) {
		final Optional<GroupBuy> optionalGroupBuy = groupBuyService.findById(groupBuyId);
		if (optionalGroupBuy.isEmpty()) {
			return rq.historyBack("존재하지 않는 공동구매 입니다.");
		}

		final GroupBuyDTO groupBuyDTO = GroupBuyDTO.createGroupBuyDTO(
			optionalGroupBuy.get(),
			groupBuyMemberService.getMemberCount(groupBuyId),
			groupBuyMemberService.isExistGroupByMember(optionalGroupBuy.get(), rq.getMember())
		);

		model.addAttribute("groupBuy", groupBuyDTO);

		return "groupBuy/detail";
	}
}

