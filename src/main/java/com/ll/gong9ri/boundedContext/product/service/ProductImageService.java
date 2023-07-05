package com.ll.gong9ri.boundedContext.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.image.entity.ProductImage;
import com.ll.gong9ri.boundedContext.product.dto.ProductImageDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;
import com.ll.gong9ri.boundedContext.product.repository.ProductImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductImageService {

	private final ProductImageRepository repository;

	public Optional<ProductImage> findById(final Long id) {
		return repository.findById(id);
	}

	public List<ProductImageDTO> getProductImages(final Long productId) {
		// return repository.findByProductId(productId);
		return null;
	}

	@Transactional
	public RsData<ProductImage> create(final Product product, final ProductImageDTO dto) {
		return null;
	}

	@Transactional
	public RsData<ProductOption> delete(final Long id) {
		return null;
	}

	private ProductImage findByIdFromExists(final List<ProductImage> images, final Long imageId) {
		return images.stream()
			.filter(image -> image.getId().equals(imageId))
			.findFirst()
			.orElse(null);
	}

	@Transactional
	public List<ProductImage> writeImages(final Product product, final List<ProductImageDTO> dtos) {
		// TODO: delete
		return dtos.stream()
			.map(e -> create(product, e).getData())
			.toList();
	}
}
