package com.ll.gong9ri.boundedContext.order.entity;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.ll.gong9ri.base.baseEntity.BaseEntity;
import com.ll.gong9ri.base.tosspayments.tossConfig.TossConfig;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.store.entity.Store;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class OrderInfo extends BaseEntity {
	private String name;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Member member;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Store store;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Product product;
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private OrderStatus orderStatus = OrderStatus.PRE_CREATED;
	private Integer price;
	private String recentOrderLogId;

	/**
	 * @return add ORDER_ID_PREFIX, base64 encoded string
	 */
	public String getEncodedOrderId() {
		return Base64.getEncoder()
			.encodeToString((TossConfig.getORDER_ID_PREFIX() + getId()).getBytes(StandardCharsets.UTF_8));
	}
}
