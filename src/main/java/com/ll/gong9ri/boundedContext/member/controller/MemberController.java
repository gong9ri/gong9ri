package com.ll.gong9ri.boundedContext.member.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.dto.MemberInfoForm;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.service.MemberService;
import com.ll.gong9ri.boundedContext.store.dto.StoreJoinDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final Rq rq;

	@PreAuthorize("isAnonymous()")
	@GetMapping("/login")
	public String showLogin() {
		return "usr/member/login";
	}

	@GetMapping("/storeForm")
	public String storeJoinForm() {
		return "usr/member/storeForm";
	}

	@PostMapping("/store")
	@PreAuthorize("not hasAuthority('store')")
	public String storeJoin(@Valid StoreJoinDTO dto) {
		final RsData<Member> rsStore = memberService.storeJoin(dto.getUsername(), dto.getPassword());

		return rq.redirectWithMsg("/", rsStore);
	}

	@PostMapping("/nickname")
	@PreAuthorize("isAuthenticated()")
	public String setNickname(@Valid MemberInfoForm form) {
		final RsData<Member> rsMember = memberService.setNickname(rq.getMember(), form.getNickname());
		if (rsMember.isFail())
			return rq.historyBack(rsMember);

		return rq.redirectWithMsg("/", rsMember);
	}

	@GetMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public String showMyPage() {
		return "usr/member/me";
	}

}
