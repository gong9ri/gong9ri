package com.ll.gong9ri.boundedContext.product.dto;

import com.ll.gong9ri.boundedContext.product.entity.Product;
import com.ll.gong9ri.boundedContext.productImage.entity.ProductImage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class ProductDTO {
    @NotBlank
    private String name;
    @NotNull
    private Integer price;
    private String description;
    private Integer maxPurchaseNum;
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();

    public Product toEntity() {
        return Product.builder()
                .name(name)
                .price(price)
                .description(description)
                .productImages(images)
                .maxPurchaseNum(maxPurchaseNum)
                .build();
    }
}
