package com.ll.gong9ri.boundedContext.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DetailDTO {
    private Long productId;
    private String name;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

}
