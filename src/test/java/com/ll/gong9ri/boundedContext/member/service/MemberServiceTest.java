package com.ll.gong9ri.boundedContext.member.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.AuthLevel;
import com.ll.gong9ri.boundedContext.member.entity.Member;

@SpringBootTest
class MemberServiceTest {
	@Autowired
	private MemberService memberService;

	@Test
	@DisplayName("default member join test")
	void memberJoinTest() {
		final String username = "dasdasdasd";
		RsData<Member> rsMember = memberService.join(username, username + username);

		assertThat(rsMember.isSuccess()).isTrue();
		assertThat(rsMember.getData().getUsername()).isEqualTo(username);
	}

	@Test
	@DisplayName("store member join test")
	void storeJoinTest() {
		final String username = "asddd";
		RsData<Member> rsMember = memberService.storeJoin(username, username + username);

		assertThat(rsMember.isSuccess()).isTrue();
		assertThat(rsMember.getData().getUsername()).isNotEqualTo(username);
		assertThat(rsMember.getData().getUsername()).contains("GONG9");
		assertThat(rsMember.getData().getGrantedAuthorities())
			.contains(new SimpleGrantedAuthority(AuthLevel.STORE.getValue()));
		assertThat(rsMember.getData().getGrantedAuthorities())
			.doesNotContain(new SimpleGrantedAuthority(AuthLevel.ADMIN.getValue()));
	}
}
