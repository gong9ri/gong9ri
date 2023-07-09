package com.ll.gong9ri.boundedContext.memberAttr.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.ll.gong9ri.base.baseEntity.BaseEntity;
import com.ll.gong9ri.boundedContext.member.entity.Member;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
public class MemberAttr extends BaseEntity {
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	@ToString.Exclude
	private Member member;
	private LocalDateTime lastVisitTime;
	@Builder.Default
	private Integer point = 0;

	public String formattedLastVisitTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 (E)");
		return lastVisitTime.format(formatter);
	}

	/**
	 * @return 24시간이 아닌, 날짜로 하루가 지났는 지를 확인합니다.
	 */
	public boolean isOneDayPassedSinceLastVisit() {
		return lastVisitTime == null
			|| LocalDateTime.now().isAfter(lastVisitTime.plusDays(1).withHour(0).withMinute(0).withSecond(0));
	}
}
