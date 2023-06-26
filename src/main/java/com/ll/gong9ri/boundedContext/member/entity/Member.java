package com.ll.gong9ri.boundedContext.member.entity;

import java.util.Arrays;
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
	@Enumerated(EnumType.STRING)
	private ProviderTypeCode providerTypeCode;
	@Enumerated(EnumType.STRING)
	private AuthLevel authLevel;
	@Column(unique = true, nullable = false)
	private String username;
	@Column(nullable = false)
	private String password;

	/**
	 * 내 권한 보다 낮은 권한들을 전부 획득하여 리턴합니다.
	 *
	 * @return List<SimpleGrantedAuthority>
	 */
	public List<GrantedAuthority> getGrantedAuthorities() {
		return Arrays.stream(AuthLevel.values()).filter(e -> e.getCode() <= authLevel.getCode())
			.map(e -> (GrantedAuthority)(new SimpleGrantedAuthority(e.getValue())))
			.toList();
	}

	public String getNickname() {
		return "%1$4s".formatted(Long.toString(getId(), 36)).replace(' ', '0');
	}
}