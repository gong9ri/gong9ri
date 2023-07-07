package com.ll.gong9ri.boundedContext.groupBuy.service;

import java.util.ArrayList;
import java.util.List;

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

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class GroupBuyServiceTest {
	@Autowired
	GroupBuyService groupBuyService;
	@Autowired
	private MemberService memberService;

	@Test
	@DisplayName("공동구매 참여인원 테스트")
	void groupBuyMemberCount() throws Exception{
		RsData<Member> user1 = memberService.join("user1", "1234");
		RsData<Member> user2 = memberService.join("user2", "1234");
		RsData<Member> user3 = memberService.join("user3", "1234");
		// TODO : 테스트 케이스 작성
	}

}
