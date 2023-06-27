package com.ll.gong9ri.boundedContext.product.service;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;
import com.ll.gong9ri.boundedContext.product.repository.ProductRepository;
import com.ll.gong9ri.boundedContext.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public RsData<Product> registerProduct(final Store store, final ProductDTO productDTO) {
        Product product = Product.of(store, productDTO);

        List<String> optionOneDetails = Optional.ofNullable(productDTO.getOptionOneDetails())
                .orElse(Collections.emptyList());
        List<String> optionTwoDetails = Optional.ofNullable(productDTO.getOptionTwoDetails())
                .orElse(Collections.emptyList());

        createProductOptionStream(product, optionOneDetails, optionTwoDetails)
                .forEach(product::addProductOption);

        productRepository.save(product);
        return RsData.of("S-1", "상품이 성공적으로 등록되었습니다.", product);
    }

    private Stream<ProductOption> createProductOptionStream(Product product, List<String> optionOneDetails, List<String> optionTwoDetails) {
        if (optionOneDetails == null || optionOneDetails.isEmpty()) {
            return Stream.empty();
        }

        if (optionTwoDetails == null || optionTwoDetails.isEmpty()) {
            return optionOneDetails.stream()
                    .map(optionOneDetail -> createProductOption(product, optionOneDetail, null));
        }

        return optionOneDetails.stream()
                .flatMap(optionOneDetail -> optionTwoDetails.stream()
                        .map(optionTwoDetail -> createProductOption(product, optionOneDetail, optionTwoDetail)));
    }

    private ProductOption createProductOption(Product product, String optionOneName, String optionTwoName) {
        return ProductOption.builder()
                .product(product)
                .optionOneName(optionOneName)
                .optionTwoName(optionTwoName)
                .build();
    }
}
