package com.ll.gong9ri.boundedContext.member.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.dto.MemberJoinDTO;
import com.ll.gong9ri.boundedContext.member.dto.StoreJoinDTO;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final Rq rq;

	@GetMapping("/join")
	public String joinForm() {
		return "usr/member/joinForm";
	}

	@PostMapping("/member")
	@PreAuthorize("isAnonymous()")
	public String storeJoin(@Valid MemberJoinDTO dto) {
		final RsData<Member> rsStore = memberService.join(dto.getUsername(), dto.getPassword());

		return rq.redirectWithMsg("/", rsStore);
	}

	@GetMapping("/storeForm")
	public String storeJoinForm() {
		return "usr/member/storeForm";
	}

	@PostMapping("/store")
	@PreAuthorize("isAnonymous()") // TODO: isStore?
	public String storeJoin(@Valid StoreJoinDTO dto) {
		final RsData<Member> rsStore = memberService.storeJoin(dto.getUsername(), dto.getPassword());

		return rq.redirectWithMsg("/", rsStore);
	}
}
