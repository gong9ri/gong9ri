package com.ll.gong9ri.boundedContext.product.entity;

import com.ll.gong9ri.base.baseEntity.BaseEntity;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.productImage.entity.ProductImage;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class Product extends BaseEntity {
	@ManyToOne
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
	@ToString.Exclude
	@Builder.Default
	private List<ProductImage> productImages = new ArrayList<>();
	@Setter
	private String optionOne;
	@Setter
	private String optionTwo;
	private Integer maxPurchaseNum;
	@OneToMany(mappedBy = "product", cascade = {CascadeType.ALL})
	@LazyCollection(LazyCollectionOption.EXTRA)
	@ToString.Exclude
	@Builder.Default
	private List<ProductOption> productOptions = new ArrayList<>();

	public void addProductOption(final ProductOption productOption) {
		this.productOptions.add(productOption);
	}

	public void addProductOptions(final List<ProductOption> productOptions) {
		this.productOptions.addAll(productOptions);
	}

	public ProductDTO toDTO() {
		return ProductDTO.builder()
				.id(this.getId())
				.name(this.name)
				.price(this.price)
				.description(this.description)
				.images(this.productImages)
				.maxPurchaseNum(this.maxPurchaseNum)
				.build();
	}
}
