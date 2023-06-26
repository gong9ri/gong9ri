package com.ll.gong9ri.boundedContext.store;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.member.entity.AuthLevel;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.member.service.MemberService;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import com.ll.gong9ri.boundedContext.store.service.StoreService;

@SpringBootTest
class StoreServiceTest {
	@Autowired
	private MemberService memberService;
	@Autowired
	private StoreService storeService;

	private Member thisMember;
	private Integer testCount = 0;

	@BeforeEach
	void beforeEach() {
		thisMember = memberService.join("who" + ++testCount, "1234").getData();
	}

	@Test
	@DisplayName("store direct create test")
	void createStore() throws Exception {
		final String storePrefix = "ImAStore";
		RsData<Store> rsStore = storeService.create(thisMember, storePrefix + thisMember.getUsername());
		assertThat(rsStore.isSuccess()).isTrue();
		assertThat(rsStore.getData().getName()).contains(storePrefix);
	}

	@Test
	@DisplayName("store created by request test")
	void createStoreByMemberRequest() throws Exception {
		final String username = "s211r";
		final String password = "1234";
		RsData<Member> rsMember = memberService.storeJoin(username, password);
		assertThat(rsMember.isSuccess()).isTrue();
		assertThat(rsMember.getData().getGrantedAuthorities())
			.contains(new SimpleGrantedAuthority(AuthLevel.STORE.getValue()));
		assertThat(storeService.searchByName(username)).isNotEmpty();
	}
}
