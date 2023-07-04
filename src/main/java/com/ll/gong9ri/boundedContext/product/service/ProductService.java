package com.ll.gong9ri.boundedContext.product.service;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;
import com.ll.gong9ri.boundedContext.product.dto.ProductDTO;
import com.ll.gong9ri.boundedContext.product.dto.ProductOptionDTO;
import com.ll.gong9ri.boundedContext.product.dto.SearchDTO;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductDiscount;
import com.ll.gong9ri.boundedContext.product.entity.ProductOption;
import com.ll.gong9ri.boundedContext.product.repository.ProductDiscountRepository;
import com.ll.gong9ri.boundedContext.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductDiscountRepository productDiscountRepository;

    @Transactional
    public RsData<Product> registerProduct(final ProductDTO productDTO) {
        Product product = productDTO.toEntity();

        saveProductDiscount(product.getProductDiscounts());

        productRepository.save(product);


        return RsData.of("S-1", "상품이 성공적으로 등록되었습니다.", product);
    }

    @Transactional
    public RsData<Product> addOptionDetails(Product product, final ProductOptionDTO productOptionDTO) {
        product.setOptionOne(productOptionDTO.getOptionOneName());
        product.setOptionTwo(productOptionDTO.getOptionTwoName());

        product.addProductOptions(
                createProductOptions(product, productOptionDTO.getOptionOneDetails(), productOptionDTO.getOptionTwoDetails())
        );

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

    public RsData<List<Product>> search(SearchDTO searchDTO) {
        List<Product> products = productRepository.findDistinctByNameLike(searchDTO.wrapWithWildcard());
        return RsData.of("S-1", "상품 검색에 성공했습니다.", products);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * 상품의 할인율을 저장하는 메서드 입니다.
     * 인자로 'productDiscounts'를 전달받아 이미 DB에 저장되어 있는지 확인하고,
     * 저장되어 있지 않을 때만 데이터를 저장합니다.
     * @param productDiscounts
     * @return 해당 메서드를 통해 저장된 ProductDiscount 객체의 List를 포함하는 RsData 객체를 반환합니다.
     */
    @Transactional
    public RsData<List<ProductDiscount>> saveProductDiscount(List<ProductDiscount> productDiscounts) {
        List<ProductDiscount> unsavedProductDiscountList = productDiscounts.stream()
                .filter(e -> productDiscountRepository.findByHeadCountAndDiscountRate(e.getHeadCount(), e.getDiscountRate()).isEmpty())
                .toList();
        productDiscountRepository.saveAll(unsavedProductDiscountList);

        return RsData.of("S-1", "상품의 할인율이 성공적으로 등록됐습니다.", unsavedProductDiscountList);
    }

    /**
     * product의 공동구매 생성 가능 여부를 리턴
     * @param productId
     * @return product의 GroupBuy목록 중 상태가 PROGRESS인 GroupBuy가 존재하면 false
     */
    public boolean canCreateGroupBuy(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            return !hasInProgressGroupBuy(product);
        }
        return false;
    }

    private boolean hasInProgressGroupBuy(Product product) {
        return product.getGroupBuys().stream()
            .anyMatch(groupBuy -> groupBuy.getStatus() == GroupBuyStatus.PROGRESS);
    }

}
