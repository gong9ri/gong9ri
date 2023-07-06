package com.ll.gong9ri.boundedContext.groupBuyChatRoom.entity;

import com.ll.gong9ri.base.baseEntity.BaseEntity;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class GroupBuyChatRoom extends BaseEntity {

	@OneToOne(fetch = FetchType.LAZY)
	private GroupBuy groupBuy;
	private String name;
	private String notice;
}
