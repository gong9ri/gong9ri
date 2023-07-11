package com.ll.gong9ri.boundedContext.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.image.dto.ImageDTO;
import com.ll.gong9ri.boundedContext.image.service.ImageService;
import com.ll.gong9ri.boundedContext.product.entity.ProductImage;
import com.ll.gong9ri.boundedContext.product.dto.ProductImageDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;
import com.ll.gong9ri.boundedContext.product.repository.ProductImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductImageService {

	private final ProductImageRepository productImageRepository;
	private final ImageService imageService;

	public Optional<ProductImage> findById(final Long id) {
		return productImageRepository.findById(id);
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
	public List<ProductImage> uploadProductImages(final Product product, final List<MultipartFile> images) {
		List<ImageDTO> dtos = imageService.uploadProductImages(images, product.getId());

		List<ProductImage> productImages = product.getImages();

		if (dtos.isEmpty() || dtos == null) {
			return productImages;
		}

		List<ProductImage> newProductImages = new ArrayList<>();

		for (ImageDTO dto : dtos) {
			ProductImage productImage = ProductImage.builder()
				.product(product)
				.fileName(dto.getUploadFileName())
				.filePath(dto.getUploadFilePath())
				.build();

			newProductImages.add(productImage);
		}
		productImages.addAll(productImageRepository.saveAll(productImages));
		return productImages;
	}
}
