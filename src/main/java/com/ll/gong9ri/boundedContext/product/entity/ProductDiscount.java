package com.ll.gong9ri.boundedContext.product.entity;

import com.ll.gong9ri.base.baseEntity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class ProductDiscount extends BaseEntity {
	@Builder.Default
	@Column(nullable = false)
	private Integer headCount = 0;
	// TODO: is this percentage?
	@Builder.Default
	@Column(nullable = false)
	private Integer salePrice = 0;
}
