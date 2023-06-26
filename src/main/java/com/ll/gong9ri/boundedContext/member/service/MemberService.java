package com.ll.gong9ri.boundedContext.member.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.entity.ProviderTypeCode;
import com.ll.gong9ri.boundedContext.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 아래 메서드들이 전부 readonly 라는 것을 명시, 나중을 위해
public class MemberService {
	private final PasswordEncoder passwordEncoder;

	private final MemberRepository memberRepository;

	public Optional<Member> findByUsername(String username) {
		return memberRepository.findByUsername(username);
	}

	@Transactional
	public RsData<Member> join(String username, String password) {
		return join(ProviderTypeCode.GONG9RI, username, password);
	}

	// 내부 처리함수, 일반회원가입, 소셜로그인을 통한 회원가입(최초 로그인 시 한번만 발생)에서 이 함수를 사용함
	private RsData<Member> join(ProviderTypeCode providerTypeCode, String username, String password) {
		if (findByUsername(username).isPresent()) {
			return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(username));
		}

		// 소셜 로그인을 통한 회원가입에서는 비번이 없다.
		if (StringUtils.hasText(password))
			password = passwordEncoder.encode(password);

		Member member = Member
			.builder()
			.providerTypeCode(providerTypeCode)
			.username(username)
			.password(password)
			.build();

		memberRepository.save(member);

		return RsData.of("S-1", "회원가입이 완료되었습니다.", member);
	}

	// 소셜 로그인(카카오, 구글, 네이버) 로그인이 될 때 마다 실행되는 함수
	@Transactional
	public RsData<Member> whenSocialLogin(ProviderTypeCode providerTypeCode, String username) {
		Optional<Member> opMember = findByUsername(username);

		return opMember.map(member -> RsData.of("S-2", "로그인 되었습니다.", member))
			.orElseGet(() -> join(providerTypeCode, username, ""));

	}
}