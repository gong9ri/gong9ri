package com.ll.gong9ri.base.event;

import com.ll.gong9ri.boundedContext.member.entity.Member;
import com.ll.gong9ri.boundedContext.product.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventGroupBuyProgress {
	private Member member;
	private Product product;
	private Integer salePrice;
}
