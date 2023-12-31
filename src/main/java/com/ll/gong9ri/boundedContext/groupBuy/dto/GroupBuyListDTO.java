package com.ll.gong9ri.boundedContext.groupBuy.dto;

import java.time.LocalDateTime;

import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;
import com.ll.gong9ri.boundedContext.product.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GroupBuyListDTO {
	private Long id;
	private String name;
	private Integer price;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private GroupBuyStatus status;
	private Integer currentMemberCount;
	private Integer nextMemberCount;
	private Integer currentSalePrice;
	private Integer nextSalePrice;
	private Boolean isParticipate;
	private Product product;
}
