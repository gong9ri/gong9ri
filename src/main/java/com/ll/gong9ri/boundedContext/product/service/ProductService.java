package com.ll.gong9ri.boundedContext.product.service;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;
import com.ll.gong9ri.boundedContext.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public RsData<Product> registerProduct(final ProductDTO productDTO) {
        Product product = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .productImages(productDTO.getImages())
                .optionOne(productDTO.getOptionOneName())
                .optionTwo(productDTO.getOptionTwoName())
                .maxPurchaseNum(productDTO.getMaxPurchaseNum())
                .build();

        productDTO.getOptionOneDetails().stream().flatMap(optionOneDetail ->
                productDTO.getOptionTwoDetails().stream().map(optionTwoDetail ->
                        ProductOption.builder()
                                .product(product)
                                .optionOneName(optionOneDetail)
                                .optionTwoName(optionTwoDetail)
                                .build()
                )
        ).forEach(product::addProductOption);

        productRepository.save(product);
        return RsData.of("S-1", "상품이 성공적으로 등록되었습니다.", product);
    }
}
