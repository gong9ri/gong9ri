package com.ll.gong9ri.boundedContext.product.service;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionNameDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;
import com.ll.gong9ri.boundedContext.product.repository.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductOptionService {
	private static final String NO_OPTION_DEFAULT = "기본 옵션";
	private final ProductOptionRepository repository;

	private RsData<ProductOption> validate(final Long id) {
		return repository.findByIdAndDeleteStatusFalse(id)
			.map(RsData::successOf)
			.orElse(RsData.failOf(null));
	}

	public List<ProductOptionNameDTO> getProductOptions(final Long productId) {
		return repository.findAllByProductIdAndDeleteStatusFalse(productId)
			.stream()
			.map(e -> ProductOptionNameDTO.builder()
				.id(e.getId())
				.optionOneName(e.getOptionOneName())
				.optionTwoName(e.getOptionTwoName())
				.build())
			.toList();
	}

	@Transactional
	public RsData<ProductOption> defaultCreate(final Product product) {
		final ProductOption productOption = ProductOption.builder()
			.product(product)
			.optionOneName(NO_OPTION_DEFAULT)
			.build();

		repository.save(productOption);

		return RsData.successOf(productOption);
	}

	@Transactional
	public RsData<ProductOption> create(final Product product, final ProductOptionNameDTO dto) {
		final ProductOption productOption = ProductOption.builder()
			.product(product)
			.optionOneName(dto.getOptionOneName())
			.optionTwoName(dto.getOptionOneName())
			.build();

		repository.save(productOption);

		return RsData.successOf(productOption);
	}

	@Transactional
	public RsData<ProductOption> delete(final Long id) {
		RsData<ProductOption> rsOption = validate(id);
		if (rsOption.isFail()) {
			return rsOption;
		}

		ProductOption productOption = rsOption.getData().toBuilder()
			.deleteStatus(Boolean.TRUE)
			.build();

		repository.save(productOption);

		return RsData.successOf(null);
	}

	@Transactional
	public void hardDelete(final Long id) {
		repository.deleteById(id);
	}

	private ProductOption findByIdFromExists(final List<ProductOption> options, final Long optionId) {
		return options.stream()
			.filter(option -> option.getId().equals(optionId))
			.findFirst()
			.orElse(null);
	}

	@Transactional
	public List<ProductOption> writeOptions(final Product product, final ProductOptionDTO dto) {
		return dto.getOptionNames().stream()
			.map(optionName -> (optionName.getId() == null) // TODO: update checker
				? create(product, optionName)
				: delete(optionName.getId()))
			.map(RsData::getData)
			.filter(Objects::nonNull)
			.toList();
	}
}
