package com.ll.gong9ri.boundedContext.product.dto;

import com.ll.gong9ri.boundedContext.image.entity.ProductImage;
import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.product.entity.ProductDiscount;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Builder
@Getter
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Integer price;
    private String description;
    private Integer maxPurchaseNum;
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();
    @NotNull
    private List<Integer> headCounts;
    @NotNull
    private List<Integer> salePrices;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public Product toEntity() {
        return Product.builder()
                .name(this.name)
                .price(this.price)
                .description(this.description)
                .images(this.images)
                .maxPurchaseNum(this.maxPurchaseNum)
                .productDiscounts(createProductDiscountList())
                .build();
    }

    public static ProductDTO toDTO(Product product) {
        List<ProductDiscount> discounts = product.getProductDiscounts();

        List<Integer> headCounts = new ArrayList<>();
        List<Integer> salePrices = new ArrayList<>();

        discounts.forEach(discount -> {
            headCounts.add(discount.getHeadCount());
            salePrices.add(discount.getSalePrice());
        });

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .images(product.getImages())
                .maxPurchaseNum(product.getMaxPurchaseNum())
                .headCounts(headCounts)
                .salePrices(salePrices)
                .createDate(product.getCreateDate())
                .modifyDate(product.getModifyDate())
                .build();
    }

    private List<ProductDiscount> createProductDiscountList() {
        return IntStream.range(0, headCounts.size())
                .mapToObj(i -> ProductDiscount.builder()
                        .headCount(headCounts.get(i))
                        .salePrice(salePrices.get(i))
                        .build()
                )
                .collect(Collectors.toUnmodifiableList());
    }

    public List<ProductDiscount> getProductDiscountList() {
        return createProductDiscountList();
    }
}
