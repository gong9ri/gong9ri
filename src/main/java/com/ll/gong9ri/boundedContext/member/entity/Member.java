package com.ll.gong9ri.boundedContext.member.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.ll.gong9ri.base.baseEntity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {
	// TODO: DB values
	@Enumerated(EnumType.STRING)
	private ProviderTypeCode providerTypeCode;
	@Column(unique = true)
	private String username;
	private String password;

	public List<? extends GrantedAuthority> getGrantedAuthorities() {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

		grantedAuthorities.add(new SimpleGrantedAuthority("member"));

		// username이 admin인 회원은 추가로 admin 권한도 가진다.
		if (isAdmin()) {
			grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
		}

		return grantedAuthorities;
	}

	public boolean isAdmin() {
		return "admin".equals(username);
	}

	public String getNickname() {
		return "%1$4s".formatted(Long.toString(getId(), 36)).replace(' ', '0');
	}
}