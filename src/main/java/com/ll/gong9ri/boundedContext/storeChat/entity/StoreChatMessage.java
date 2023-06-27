package com.ll.gong9ri.boundedContext.storeChat.entity;

import com.ll.gong9ri.base.baseEntity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class StoreChatMessage extends BaseEntity {
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private StoreChatRoom storeChatRoom;
	@Column(columnDefinition = "TEXT")
	private String content;
	@Column(nullable = false)
	private Boolean sentByStore;
}
