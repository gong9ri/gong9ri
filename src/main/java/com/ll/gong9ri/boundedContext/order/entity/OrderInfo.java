package com.ll.gong9ri.boundedContext.order.entity;

import com.ll.gong9ri.base.baseEntity.BaseEntity;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.store.entity.Store;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class OrderInfo extends BaseEntity {
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false)
	private Member member;
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false)
	private Store store;
	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus orderStatus = OrderStatus.NOT_ACCEPTED;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	@ToString.Exclude
	private OrderRecipientInfo orderRecipientInfo;
}
