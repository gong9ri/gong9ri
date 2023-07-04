package com.ll.gong9ri.boundedContext.memberAttr.controller;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.memberAttr.dto.MemberMeHomeDTO;
import com.ll.gong9ri.boundedContext.memberAttr.entity.MemberAttr;
import com.ll.gong9ri.boundedContext.memberAttr.service.MemberAttrService;
import com.ll.gong9ri.boundedContext.review.service.ReviewService;
import com.ll.gong9ri.boundedContext.storeChat.service.StoreChatRoomService;

import lombok.RequiredArgsConstructor;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/me")
@RequiredArgsConstructor
public class MemberPageController {
	private final MemberAttrService memberAttrService;
	private final StoreChatRoomService roomService;
	private final ReviewService reviewService;
	private final Rq rq;

	@GetMapping("/")
	public String home(Model model) {
		final Optional<MemberAttr> oAttr = memberAttrService.findByMemberId(rq.getMember().getId());
		if (oAttr.isEmpty()) {
			return rq.historyBack("잘못된 접근입니다.");
		}

		final MemberMeHomeDTO dto = MemberMeHomeDTO.builder()
			.lastVisitDate(oAttr.get().formattedLastVisitTime())
			.myPoint(oAttr.get().getPoint())
			.build();

		model.addAttribute("attr", dto);

		return "usr/member/me";
	}

	@PostMapping("/daily")
	public String dailyCheck() {
		RsData<MemberAttr> rsAttr = memberAttrService.dailyVisit(rq.getMember().getId());
		if (rsAttr.isFail()) {
			return rq.historyBack(rsAttr);
		}

		return rq.redirectWithMsg("/me", rsAttr);
	}

	@GetMapping("/chatroom/store")
	public String storeChatList(Model model) {
		model.addAttribute("rooms", roomService.getMemberChatRooms(rq.getMember().getId()));

		return "usr/store/chat/list";
	}

	@GetMapping("/chatroom/groupbuy")
	public String groupBuyChatList(Model model) {
		// gb rooms

		return "usr/store/chat/list";
	}

	@GetMapping("/review")
	public String myReviews(Model model) {
		model.addAttribute("reviews", reviewService.getMemberReviews(rq.getMember().getId()));

		return "usr/store/chat/list";
	}
}
