package com.ll.gong9ri.boundedContext.product.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchDTO {
    private String keyword;

    public String wrapWithWildcard() {
        return "%" + this.keyword + "%";
    }
}
