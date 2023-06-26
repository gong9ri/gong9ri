package com.ll.gong9ri.boundedContext.member.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ll.gong9ri.base.event.EventAfterStoreJoinAccept;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.AuthLevel;
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
	private final ApplicationEventPublisher publisher;

	@Value("${custom.store.storeNamePrefix}")
	private String storeNamePrefix;

	public Optional<Member> findByUsername(String username) {
		return memberRepository.findByUsername(username);
	}

	@Transactional
	public RsData<Member> join(String username, String password) {
		return join(ProviderTypeCode.GONG9RI, username, password);
	}

	private RsData<Member> join(ProviderTypeCode providerTypeCode, String username, String password) {
		if (findByUsername(username).isPresent()) {
			return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(username));
		}

		if (StringUtils.hasText(password))
			password = passwordEncoder.encode(password);

		Member member = Member
			.builder()
			.providerTypeCode(providerTypeCode)
			.username(username)
			.password(password)
			.authLevel(AuthLevel.MEMBER)
			.build();

		memberRepository.save(member);

		return RsData.of("S-1", "회원가입이 완료되었습니다.", member);
	}

	@Transactional
	public RsData<Member> whenSocialLogin(ProviderTypeCode providerTypeCode, String username) {
		Optional<Member> opMember = findByUsername(username);

		return opMember.map(member -> RsData.of("S-2", "로그인 되었습니다.", member))
			.orElseGet(() -> join(providerTypeCode, username, ""));

	}

	@Transactional
	public RsData<Member> storeJoin(final String storeName, String password) {
		final String storeDBName = storeNamePrefix + storeName;
		if (findByUsername(storeDBName).isPresent()) {
			return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(storeDBName));
		}

		if (StringUtils.hasText(password))
			password = passwordEncoder.encode(password);

		Member member = Member
			.builder()
			.providerTypeCode(ProviderTypeCode.GONG9RI)
			.username(storeDBName)
			.password(password)
			.authLevel(AuthLevel.STORE)
			.build();

		memberRepository.save(member);

		publisher.publishEvent(new EventAfterStoreJoinAccept(member, storeName));

		return RsData.of("S-1", "회원가입이 완료되었습니다.", member);
	}
}