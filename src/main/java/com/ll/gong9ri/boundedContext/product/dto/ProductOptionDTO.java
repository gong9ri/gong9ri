package com.ll.gong9ri.boundedContext.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class ProductOptionDTO {

    private String optionOneName;
    private String optionTwoName;
    @Builder.Default
    private List<String> optionOneDetails = new ArrayList<>();
    @Builder.Default
    private List<String> optionTwoDetails = new ArrayList<>();
}
