package com.ll.gong9ri.base.rq;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Scope(scopeName = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WsRq {
	private final MemberService memberService;
	private final HttpSession session;
	private final User user;
	private Member member = null; // 레이지 로딩, 처음부터 넣지 않고, 요청이 들어올 때 넣는다.

	public WsRq(MemberService memberService, HttpSession session) {
		this.memberService = memberService;
		this.session = session;

		// 현재 로그인한 회원의 인증정보를 가져옴
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication.getPrincipal() instanceof User aUser) {
			this.user = aUser;
		} else {
			this.user = null;
		}
	}

	// 로그인 되어 있는지 체크
	public boolean isLogin() {
		return user != null;
	}

	// 로그아웃 되어 있는지 체크
	public boolean isLogout() {
		return !isLogin();
	}

	// 로그인 된 회원의 객체
	public Member getMember() {
		if (isLogout()) {
			return null;
		}

		// 데이터가 없는지 체크
		if (member == null) {
			member = memberService.getByUsername__cached(user.getUsername());
		}

		return member;
	}
}