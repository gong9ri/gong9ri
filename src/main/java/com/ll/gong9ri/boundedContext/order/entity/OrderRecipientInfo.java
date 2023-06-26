package com.ll.gong9ri.boundedContext.order.entity;

import com.ll.gong9ri.base.baseEntity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class OrderRecipientInfo extends BaseEntity {
	@Column(nullable = false)
	private String recipient;
	@Column(nullable = false)
	@Pattern(
		regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$",
		message = "10 ~ 11 자리의 숫자만 입력 가능합니다."
	)
	private String phoneNumber;
	@Column(nullable = false)
	private String mainAddress;
	private String subAddress;
	@Column(nullable = false)
	private Integer totalPrice;
}
