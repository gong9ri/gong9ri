package com.ll.gong9ri.boundedContext.product.entity;

import com.ll.gong9ri.base.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class ProductOption extends BaseEntity {
    @Setter
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false)
    private Product product;
    private String optionOneName;
    private String optionTwoName;
}
