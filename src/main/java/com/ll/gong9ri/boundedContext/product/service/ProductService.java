package com.ll.gong9ri.boundedContext.product.service;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;
import com.ll.gong9ri.boundedContext.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public RsData<Product> registerProduct(final ProductDTO productDTO) {
        Product product = productDTO.toEntity();

        productRepository.save(product);

        return RsData.of("S-1", "상품이 성공적으로 등록되었습니다.", product);
    }

    @Transactional
    public RsData<Product> addOptionDetails(Product product, final ProductOptionDTO productOptionDTO) {
        product = product.toBuilder()
                .optionOne(productOptionDTO.getOptionOneName())
                .optionTwo(productOptionDTO.getOptionTwoName())
                .build();

        product.addProductOptions(
                createProductOptions(product, productOptionDTO.getOptionOneDetails(), productOptionDTO.getOptionTwoDetails())
        );

        productRepository.save(product);

        return RsData.of("S-1", "상품 상세 옵션이 성공적으로 등록되었습니다.", product);
    }

    private List<ProductOption> createProductOptions(
            Product product,
            List<String> optionOneDetails,
            List<String> optionTwoDetails
    ) {
        if (optionOneDetails == null || optionOneDetails.isEmpty()) {
            return Collections.emptyList();
        }

        if (optionTwoDetails == null || optionTwoDetails.isEmpty()) {
            return optionOneDetails.stream()
                    .map(optionOneDetail -> createProductOption(product, optionOneDetail, null))
                    .toList();
        }

        return optionOneDetails.stream()
                .flatMap(optionOneDetail -> optionTwoDetails.stream()
                        .map(optionTwoDetail -> createProductOption(product, optionOneDetail, optionTwoDetail)))
                .toList();
    }

    private ProductOption createProductOption(Product product, String optionOneName, String optionTwoName) {
        return ProductOption.builder()
                .product(product)
                .optionOneName(optionOneName)
                .optionTwoName(optionTwoName)
                .build();
    }

    public RsData<List<Product>> getAllProducts() {
        return RsData.of("S-1", "모든 상품의 리스트를 가져옵니다.", productRepository.findAll());
    }
}
