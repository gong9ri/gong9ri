package com.ll.gong9ri.boundedContext.member.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.ll.gong9ri.base.baseEntity.BaseEntity;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.entity.ChatRoomParticipant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
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
	private String nickname;

	@OneToMany(mappedBy = "member", cascade = {CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.EXTRA)
	@ToString.Exclude
	private List<ChatRoomParticipant> chatRoomParticipants;

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

	public void joinChat(ChatRoomParticipant chatRoomParticipant){
		if (chatRoomParticipants == null) {
			chatRoomParticipants = new ArrayList<>();
		}
		chatRoomParticipants.add(chatRoomParticipant);
	}
}