package com.ll.gong9ri.boundedContext.product.entity;

import com.ll.gong9ri.base.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class ProductOption extends BaseEntity {
    private Long productId;
    private String optionOneName;
    private String optionTwoName;
}
