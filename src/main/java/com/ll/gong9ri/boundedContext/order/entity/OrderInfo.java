package com.ll.gong9ri.boundedContext.order.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.ll.gong9ri.base.baseEntity.BaseEntity;
import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.product.entity.Product;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
	private Long memberId;
	private String username;
	private Long storeId;
	private String storeName;
	private Long productId;
	private String productName;
	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus orderStatus = OrderStatus.NOT_ACCEPTED;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	@ToString.Exclude
	private OrderRecipientInfo orderRecipientInfo;
	@OneToMany(mappedBy = "orderInfo", cascade = {CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.EXTRA)
	@ToString.Exclude
	@Builder.Default
	private List<ProductOptionQuantity> productOptionQuantities = new ArrayList<>();

	public void addProductOptionQuantity(ProductOptionQuantity productOptionQuantity) {
		productOptionQuantities.add(productOptionQuantity);
	}

	public static OrderInfo of(Member member, Product product) {
		return OrderInfo.builder()
			.memberId(member.getId())
			.username(member.getUsername())
			.storeId(product.getStore().getId())
			.storeName(product.getStore().getName())
			.productId(product.getId())
			.productName(product.getName())
			.build();
	}
}
