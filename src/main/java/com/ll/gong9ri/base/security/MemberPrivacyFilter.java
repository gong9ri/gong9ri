package com.ll.gong9ri.base.security;

import java.io.IOException;
import java.util.Arrays;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.RequiredArgsConstructor;

@WebFilter(urlPatterns = "/member/privacy/create")
@RequiredArgsConstructor
public class MemberPrivacyFilter implements Filter {
	private final MemberEncryptionUtil memberEncryptionUtil;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest)request);
		chain.doFilter(requestWrapper, response);
	}

	private class RequestWrapper extends HttpServletRequestWrapper {
		public RequestWrapper(HttpServletRequest request) {
			super(request);
		}

		@Override
		public String[] getParameterValues(String parameter) {
			String[] values = super.getParameterValues(parameter);

			return values == null
				? new String[0]
				: Arrays.stream(values)
				.map(memberEncryptionUtil::encrypt)
				.toArray(String[]::new);
		}

		@Override
		public String getParameter(String parameter) {
			String value = super.getParameter(parameter);

			return value == null ? null : memberEncryptionUtil.encrypt(value);
		}
	}
}