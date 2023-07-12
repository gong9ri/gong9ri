package com.ll.gong9ri.boundedContext.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductDiscountDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductRegisterDTO;
import com.ll.gong9ri.boundedContext.product.dto.SearchDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductDiscount;
import com.ll.gong9ri.boundedContext.product.entity.ProductImage;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;
import com.ll.gong9ri.boundedContext.product.repository.ProductRepository;
import com.ll.gong9ri.boundedContext.store.entity.Store;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
	private final ProductRepository repository;
	private final ProductOptionService optionService;
	private final ProductDiscountService discountService;
	private final ProductImageService productImageService;

	public Optional<Product> getProduct(final Long id) {
		return repository.findById(id);
	}

	public RsData<ProductDTO> getProductDetail(final Long id) {
		return repository.findById(id)
			.map(ProductDTO::toDTO)
			.map(RsData::successOf)
			.orElse(RsData.failOf(null));
	}

	public RsData<List<Product>> getAllProducts() {
		return RsData.of("S-1", "모든 상품의 리스트를 가져옵니다.", repository.findAll());
	}

	public List<Product> getAllProductsByStore(final Long storeId) {
		return repository.findByStoreId(storeId);
	}

	public RsData<List<Product>> getProductsByStoreId(final Long storeId) {
		List<Product> products = repository.findByStoreId(storeId);
		return RsData.of("S-1", "해당 스토어의 모든 상품 리스트를 가져옵니다.", products);
	}

	public RsData<List<Product>> search(SearchDTO searchDTO) {
		List<Product> products = repository.findDistinctByNameLike(searchDTO.wrapWithWildcard());
		return RsData.of("S-1", "상품 검색에 성공했습니다.", products);
	}

	public Boolean storeValidation(final Long id, final Long storeId) {
		return repository.existsByIdAndStoreId(id, storeId);
	}

	@Transactional
	public RsData<Product> registerProduct(final Store store, @Valid ProductRegisterDTO productRegisterDTO) {
		Product product = Product.builder()
			.name(productRegisterDTO.getName())
			.price(productRegisterDTO.getPrice())
			.description(productRegisterDTO.getDescription())
			.maxPurchaseNum(productRegisterDTO.getMaxPurchaseNum())
			.store(store)
			.build();

		repository.save(product);

		addImages(product.getId(), productRegisterDTO.getImages());

		optionService.defaultCreate(product);

		return RsData.of("S-1", "상품이 성공적으로 등록되었습니다.", product);
	}
	@Transactional
	public RsData<Product> addImages(final Long id, final List<MultipartFile> images) {
		Optional<Product> unModifiedProduct = repository.findById(id);
		if (unModifiedProduct.isEmpty()) {
			return RsData.failOf(null);
		}

		List<ProductImage> productImages = productImageService.uploadProductImages(unModifiedProduct.get(), images);

		Product product = unModifiedProduct.get().toBuilder()
			.images(productImages)
			.build();

		return RsData.of("S-1", "상품 이미지가 성공적으로 등록되었습니다.", repository.save(product));
	}

	@Transactional
	public RsData<Product> addOptions(final Long id, final ProductOptionDTO productOptionDTO) {
		Optional<Product> unModifiedProduct = repository.findById(id);
		if (unModifiedProduct.isEmpty()) {
			return RsData.failOf(null);
		}

		List<ProductOption> options = optionService.writeOptions(unModifiedProduct.get(), productOptionDTO);

		Product product = unModifiedProduct.get().toBuilder()
			.optionName(productOptionDTO.getOptionName())
			.productOptions(options)
			.build();

		repository.save(product);

		return RsData.of("S-1", "상품 상세 옵션이 성공적으로 등록되었습니다.", product);
	}

	@Transactional
	public RsData<Product> addDiscounts(final Long id, final List<ProductDiscountDTO> dtos) {
		Optional<Product> unModifiedProduct = repository.findById(id);
		if (unModifiedProduct.isEmpty()) {
			return RsData.failOf(null);
		}

		List<ProductDiscount> discounts = discountService.writeDiscounts(unModifiedProduct.get(), dtos);

		Product product = unModifiedProduct.get().toBuilder()
			.productDiscounts(discounts)
			.build();

		repository.save(product);

		return RsData.of("S-1", "인원 별 할인이 성공적으로 등록되었습니다.", product);
	}

	public Integer getCountByStore(final Long storeId) {
		return repository.countByStoreId(storeId);
	}
}
