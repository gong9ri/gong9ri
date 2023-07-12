package com.ll.gong9ri.boundedContext.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.dto.ProductDiscountDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductDiscount;
import com.ll.gong9ri.boundedContext.product.repository.ProductDiscountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductDiscountService {
	private final ProductDiscountRepository repository;

	public Optional<ProductDiscount> findById(final Long id) {
		return repository.findByIdAndDeleteStatusFalse(id);
	}

	public List<ProductDiscountDTO> getProductDiscounts(final Long productId) {
		return repository.findAllByProductIdAndDeleteStatusFalse(productId)
			.stream()
			.map(e -> ProductDiscountDTO.builder()
				.headCount(e.getHeadCount())
				.salePrice(e.getSalePrice())
				.build())
			.toList();
	}

	@Transactional
	public RsData<ProductDiscount> create(final Product product, final ProductDiscountDTO dto) {
		final ProductDiscount productDiscount = ProductDiscount.builder()
			.product(product)
			.headCount(dto.getHeadCount())
			.salePrice(dto.getSalePrice())
			.build();

		repository.save(productDiscount);

		return RsData.successOf(productDiscount);
	}

	@Transactional
	public RsData<ProductDiscount> delete(final Long id) {
		final Optional<ProductDiscount> oProductDiscount = findById(id);
		if (oProductDiscount.isEmpty()) {
			return RsData.failOf(null);
		}

		ProductDiscount productDiscount = oProductDiscount.get().toBuilder()
			.deleteStatus(Boolean.TRUE)
			.build();

		repository.save(productDiscount);

		return RsData.successOf(null);
	}

	@Transactional
	public void hardDelete(final Long id) {
		repository.deleteById(id);
	}

	private Boolean isModified(final List<ProductDiscount> discounts, final ProductDiscountDTO dto) {
		if (dto.getId() == null) {
			return false;
		}

		final ProductDiscount productDiscount = discounts
			.stream().filter(e -> e.getId().equals(dto.getId()))
			.findAny().orElse(null);

		if (productDiscount == null) {
			return false;
		}

		return !productDiscount.getHeadCount().equals(dto.getHeadCount())
			|| !productDiscount.getSalePrice().equals(dto.getSalePrice());
	}

	@Transactional
	public List<ProductDiscount> writeDiscounts(final Product product, final List<ProductDiscountDTO> dtos) {
		final List<ProductDiscount> discounts = product.getProductDiscounts();
		getUnUsedDiscounts(discounts, dtos).forEach(productDiscount -> delete(productDiscount.getId()));

		return dtos.stream()
			.map(e -> {
				final Long id = e.getId();
				if (id != null && isModified(discounts, e)) {
					delete(id);
				}
				return create(product, e).getData();
			})
			.toList();
	}

	private List<ProductDiscount> getUnUsedDiscounts(List<ProductDiscount> discounts, List<ProductDiscountDTO> dtos) {
		return discounts.stream()
			.filter(discount -> dtos.stream()
				.noneMatch(dto -> dto.getId() != null && dto.getId().equals(discount.getId())))
			.toList();
	}
}
