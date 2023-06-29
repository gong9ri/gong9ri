package com.ll.gong9ri.base.security.handler;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ll.gong9ri.base.jwt.JwtProvider;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.service.MemberService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final MemberService memberService;
	private final JwtProvider jwtProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		final OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		final Member member = memberService.findByUsername(oAuth2User.getName())
			.orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));

		Map<String, Object> rawClaim = new LinkedHashMap<>();
		rawClaim.put("memberId", String.valueOf(member.getId()));
		String accessToken = jwtProvider.genToken(rawClaim, 60 * 60 * 30);

		super.onAuthenticationSuccess(request, response, authentication);
	}
}
