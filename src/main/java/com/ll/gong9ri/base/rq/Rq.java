package com.ll.gong9ri.base.rq;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.service.MemberService;
import com.ll.gong9ri.standard.util.Ut;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Date;

@Component
@Slf4j
@RequestScope
public class Rq {
	private final MemberService memberService;
	private final HttpServletRequest req;
	private final HttpServletResponse resp;
	@Getter
	private final HttpSession session;
	private final User user;
	private Member member = null; // 레이지 로딩, 처음부터 넣지 않고, 요청이 들어올 때 넣는다.

	public Rq(MemberService memberService, HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
		this.memberService = memberService;
		this.req = req;
		this.resp = resp;
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
		if (isLogout())
			return null;

		// 데이터가 없는지 체크
		if (member == null) {
			member = memberService.getByUsername__cached(user.getUsername());
		}

		return member;
	}

	// 뒤로가기 + 메세지
	public String historyBack(String msg) {
		String referer = req.getHeader("referer");
		String key = "historyBackErrorMsg___" + referer;
		req.setAttribute("localStorageKeyAboutHistoryBackErrorMsg", key);
		req.setAttribute("historyBackErrorMsg", msg);
		// 200 이 아니라 400 으로 응답코드가 지정되도록
		resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return "common/js";
	}

	// 뒤로가기 + 메세지
	public <T> String historyBack(RsData<T> rsData) {
		return historyBack(rsData.getMsg());
	}

	// 302 + 메세지
	public <T> String redirectWithMsg(String url, RsData<T> rsData) {
		return redirectWithMsg(url, rsData.getMsg());
	}

	// 302 + 메세지
	public String redirectWithMsg(String url, String msg) {
		return "redirect:" + urlWithMsg(url, msg);
	}

	public String redirectWithErrorMsg(String url, String msg) {
		url = Ut.url.modifyQueryParam(url, "errorMsg", msgWithTtl(msg));
		return "redirect:" + url;
	}

	// 302 + 에러 메시지
	public <T> String redirectWithErrorMsg(String url, RsData<T> rsData) {
		url = Ut.url.modifyQueryParam(url, "errorMsg", msgWithTtl(rsData.getMsg()));

		return "redirect:" + url;
	}

	public <T> void setAttributeToSession(String attributeName, T obj) {
		this.session.setAttribute(attributeName, obj);
	}

	public Object getAttributeFromSession(String attributeName) {
		return this.session.getAttribute(attributeName);
	}

	private String urlWithMsg(String url, String msg) {
		// 기존 URL에 혹시 msg 파라미터가 있다면 그것을 지우고 새로 넣는다.
		return Ut.url.modifyQueryParam(url, "msg", msgWithTtl(msg));
	}

	// 메세지에 ttl 적용
	private String msgWithTtl(String msg) {
		return Ut.url.encode(msg) + ";ttl=" + new Date().getTime();
	}
}