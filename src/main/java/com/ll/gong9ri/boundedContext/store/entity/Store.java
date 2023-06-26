package com.ll.gong9ri.boundedContext.store.entity;

import com.ll.gong9ri.base.baseEntity.BaseEntity;

import jakarta.persistence.Column;
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
public class Store extends BaseEntity {
	@Column(nullable = false, unique = true)
	private String name;
}
