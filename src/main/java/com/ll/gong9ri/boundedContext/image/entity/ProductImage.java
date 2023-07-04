package com.ll.gong9ri.boundedContext.image.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ll.gong9ri.base.baseEntity.BaseEntity;
import com.ll.gong9ri.boundedContext.product.entity.Product;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class ProductImage extends BaseEntity {
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Product product;
}