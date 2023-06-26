package com.ll.gong9ri.base.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.entity.ProviderTypeCode;
import com.ll.gong9ri.boundedContext.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final MemberService memberService;

	private String getOAuthId(final OAuth2User oAuth2User, final ProviderTypeCode providerTypeCode) {
		if (providerTypeCode == ProviderTypeCode.NAVER)
			return ((Map<String, String>)oAuth2User.getAttributes().get("response")).get("id");

		return oAuth2User.getName();
	}

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		final ProviderTypeCode providerTypeCode = ProviderTypeCode.codeOf(
			userRequest.getClientRegistration()
				.getRegistrationId()
				.toUpperCase()
		);

		final String oauthId = getOAuthId(oAuth2User, providerTypeCode);

		final String username = providerTypeCode + "__%s".formatted(oauthId);

		final Member member = memberService.whenSocialLogin(providerTypeCode, username).getData();

		return new CustomOAuth2User(member.getUsername(), member.getPassword(), member.getGrantedAuthorities());
	}
}

class CustomOAuth2User extends User implements OAuth2User {

	public CustomOAuth2User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return Collections.emptyMap();
	}

	@Override
	public String getName() {
		return getUsername();
	}
}