package com.ll.gong9ri.base.security;

import java.io.IOException;

import com.ll.gong9ri.boundedContext.privacy.dto.PrivacyDTO;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@WebFilter("/member/privacy")
@RequiredArgsConstructor
public class MemberPrivacyFilter implements Filter {
	private PrivacyDTO retrievePrivacyDTO(HttpServletRequest request) {
		return PrivacyDTO.builder()
			.recipient(MemberEncryptionUtil.encrypt(request.getParameter("recipient")))
			.phoneNumber(MemberEncryptionUtil.encrypt(request.getParameter("phoneNumber")))
			.mainAddress(MemberEncryptionUtil.encrypt(request.getParameter("mainAddress")))
			.subAddress(MemberEncryptionUtil.encrypt(request.getParameter("subAddress")))
			.build();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {

		PrivacyDTO privacyDTO = retrievePrivacyDTO((HttpServletRequest)request);

		chain.doFilter(request, response);
	}
}