package com.ll.gong9ri.boundedContext.product.dto;

import com.ll.gong9ri.boundedContext.productImage.entity.ProductImage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
    private List<ProductImage> images;
    private String optionOneName;
    private String optionTwoName;
    private Integer maxPurchaseNum;
    private List<String> optionOneDetails;
    private List<String> optionTwoDetails;
}
