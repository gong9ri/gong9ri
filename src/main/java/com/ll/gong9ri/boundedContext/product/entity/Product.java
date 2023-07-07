package com.ll.gong9ri.boundedContext.product.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.ll.gong9ri.base.baseEntity.BaseEntity;
import com.ll.gong9ri.boundedContext.image.entity.ProductImage;
import com.ll.gong9ri.boundedContext.store.entity.Store;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Product extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Store store;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private Integer price;
	@Column(columnDefinition = "TEXT")
	private String description;
	@OneToMany(mappedBy = "product", cascade = {CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.EXTRA)
	@Builder.Default
	private List<ProductImage> images = new ArrayList<>();
	@Setter
	private String optionOne;
	@Setter
	private String optionTwo;
	private Integer maxPurchaseNum;
	@OneToMany(mappedBy = "product", cascade = {CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.EXTRA)
	@Builder.Default
	private List<ProductDiscount> productDiscounts = new ArrayList<>();
	@OneToMany(mappedBy = "product", cascade = {CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.EXTRA)
	@Builder.Default
	private List<ProductOption> productOptions = new ArrayList<>();
}
