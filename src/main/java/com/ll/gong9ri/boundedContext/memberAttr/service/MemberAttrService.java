package com.ll.gong9ri.boundedContext.memberAttr.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.memberAttr.entity.MemberAttr;
import com.ll.gong9ri.boundedContext.memberAttr.repository.MemberAttrRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberAttrService {
	private static final Integer DEFAULT_DAILY_POINT = 1;
	private final MemberAttrRepository attrRepository;

	@Transactional(readOnly = true)
	public Optional<MemberAttr> findByMemberId(final Long memberId) {
		return attrRepository.findByMemberId(memberId);
	}

	public RsData<MemberAttr> create(final Member member) {
		MemberAttr memberAttr = MemberAttr.builder()
			.member(member)
			.build();

		attrRepository.save(memberAttr);

		return RsData.successOf(memberAttr);
	}

	public RsData<MemberAttr> dailyVisit(final Long memberId) {
		final Optional<MemberAttr> oMemberAttr = findByMemberId(memberId);
		if (oMemberAttr.isEmpty()) {
			return RsData.failOf(null);
		}

		MemberAttr memberAttr = oMemberAttr.get();

		if (!memberAttr.isOneDayPassedSinceLastVisit()) {
			return RsData.of("F-2", "내일 방문해주세요", memberAttr);
		}

		memberAttr = memberAttr.toBuilder()
			.lastVisitTime(LocalDateTime.now())
			.point(memberAttr.getPoint() + DEFAULT_DAILY_POINT)
			.build();

		attrRepository.save(memberAttr);

		return RsData.of("S-1", "출석체크에 성공하여 %d 포인트가 적립됐습니다.".formatted(DEFAULT_DAILY_POINT), memberAttr);
	}
}