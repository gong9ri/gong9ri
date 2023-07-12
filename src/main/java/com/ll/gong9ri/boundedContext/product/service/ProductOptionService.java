package com.ll.gong9ri.boundedContext.product.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDetailDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;
import com.ll.gong9ri.boundedContext.product.repository.ProductOptionRepository;

import lombok.RequiredArgsConstructor;

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

	public List<ProductOptionDetailDTO> getProductOptions(final Long productId) {
		return repository.findAllByProductIdAndDeleteStatusFalse(productId)
			.stream()
			.map(e -> ProductOptionDetailDTO.builder()
				.id(e.getId())
				.optionDetail(e.getOptionDetail())
				.build())
			.toList();
	}

	@Transactional
	public RsData<ProductOption> defaultCreate(final Product product) {
		final ProductOption productOption = ProductOption.builder()
			.product(product)
			.optionDetail(NO_OPTION_DEFAULT)
			.build();

		repository.save(productOption);

		return RsData.successOf(productOption);
	}

	@Transactional
	public RsData<ProductOption> create(final Product product, final ProductOptionDetailDTO dto) {
		final ProductOption productOption = ProductOption.builder()
			.product(product)
			.optionDetail(dto.getOptionDetail())
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
		getUnUsedOptions(product, dto.getOptionDetails())
			.forEach(productOption -> delete(productOption.getId()));

		return dto.getOptionDetails().stream()
			.map(optionName -> (optionName.getId() == null)
				? create(product, optionName)
				: delete(optionName.getId()))
			.map(RsData::getData)
			.filter(Objects::nonNull)
			.toList();
	}

	private List<ProductOptionDetailDTO> getUnUsedOptions(Product product, List<ProductOptionDetailDTO> dtos) {
		List<ProductOptionDetailDTO> productOptionDetails = getProductOptions(product.getId());
		return productOptionDetails.stream()
			.filter(productOptionDetail -> dtos.stream()
				.noneMatch(dto -> dto.getId() != null && dto.getId().equals(productOptionDetail.getId())))
			.toList();
	}
}
