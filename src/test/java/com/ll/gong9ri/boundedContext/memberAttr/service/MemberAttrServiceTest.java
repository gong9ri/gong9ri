package com.ll.gong9ri.boundedContext.memberAttr.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.service.MemberService;
import com.ll.gong9ri.boundedContext.memberAttr.entity.MemberAttr;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class MemberAttrServiceTest {
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberAttrService attrService;

	@Test
	@DisplayName("daily check")
	void dailyCheckTest() {
		Member member = memberService.join("sodnfkujsdn", "1234").getData();
		Optional<MemberAttr> oMemberAttr = attrService.findByMemberId(member.getId());
		assertThat(oMemberAttr).isNotEmpty();

		final RsData<MemberAttr> rsDailySuccess = attrService.dailyVisit(member.getId());
		assertThat(rsDailySuccess.isSuccess()).isTrue();

		final RsData<MemberAttr> rsDailyFail = attrService.dailyVisit(member.getId());
		assertThat(rsDailyFail.isSuccess()).isFalse();
		assertThat(rsDailySuccess.getData().getPoint()).isEqualTo(rsDailyFail.getData().getPoint());
	}
}
