package com.ll.gong9ri.boundedContext.privacy.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.privacy.dto.PrivacyDTO;
import com.ll.gong9ri.boundedContext.privacy.entity.MemberPrivacy;
import com.ll.gong9ri.boundedContext.privacy.repository.MemberPrivacyRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberPrivacyService {
	private final MemberPrivacyRepository privacyRepository;

	@Transactional(readOnly = true)
	public Optional<MemberPrivacy> findByMemberId(final Long memberId) {
		return privacyRepository.findByMemberId(memberId);
	}

	@Transactional(readOnly = true)
	public RsData<PrivacyDTO> getPrivacy(final Long memberId) {
		return findByMemberId(memberId)
			.map(o -> PrivacyDTO.builder()
				.recipient(o.getRecipient())
				.phoneNumber(o.getPhoneNumber())
				.mainAddress(o.getMainAddress())
				.subAddress(o.getSubAddress())
				.build())
			.map(RsData::successOf)
			.orElseGet(() -> RsData.failOf(null));
	}

	public RsData<MemberPrivacy> create(final Member member, final PrivacyDTO privacyDTO) {
		MemberPrivacy memberPrivacy = MemberPrivacy.builder()
			.member(member)
			.recipient(privacyDTO.getRecipient())
			.phoneNumber(privacyDTO.getPhoneNumber())
			.mainAddress(privacyDTO.getMainAddress())
			.subAddress(privacyDTO.getSubAddress())
			.build();

		privacyRepository.save(memberPrivacy);

		return RsData.successOf(memberPrivacy);
	}

	public RsData<MemberPrivacy> update(final Member member, final PrivacyDTO privacyDTO) {
		Optional<MemberPrivacy> oMemberPrivacy = findByMemberId(member.getId());
		if (oMemberPrivacy.isEmpty()) {
			return RsData.failOf(null);
		}

		MemberPrivacy memberPrivacy = oMemberPrivacy.get()
			.toBuilder()
			.member(member)
			.recipient(privacyDTO.getRecipient())
			.phoneNumber(privacyDTO.getPhoneNumber())
			.mainAddress(privacyDTO.getMainAddress())
			.subAddress(privacyDTO.getSubAddress())
			.build();

		privacyRepository.save(memberPrivacy);

		return RsData.successOf(memberPrivacy);
	}

	public RsData<Void> delete(final Long memberId) {
		Optional<MemberPrivacy> oMemberPrivacy = findByMemberId(memberId);
		if (oMemberPrivacy.isEmpty())
			return RsData.failOf(null);
		privacyRepository.delete(oMemberPrivacy.get());

		return RsData.successOf(null);
	}
}