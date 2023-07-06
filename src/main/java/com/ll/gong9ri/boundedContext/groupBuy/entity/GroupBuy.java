package com.ll.gong9ri.boundedContext.groupBuy.entity;

import java.time.LocalDateTime;

import com.ll.gong9ri.base.baseEntity.BaseEntity;
import com.ll.gong9ri.boundedContext.product.entity.Product;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class GroupBuy extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false)
	private Product product;
	@Column(nullable = false)
	private String name;
	@Builder.Default
	@Column(nullable = false)
	private LocalDateTime startDate = LocalDateTime.now();
	@Column(nullable = false)
	private LocalDateTime endDate;
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private GroupBuyStatus status = GroupBuyStatus.WAIT;
}
