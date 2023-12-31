package com.ll.gong9ri.boundedContext.groupBuy.dto;

import java.time.LocalDateTime;

import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;
import com.ll.gong9ri.boundedContext.product.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class GroupBuyDetailDTO {
	private Long id;
	private String name;
	private Integer price;
	private String description;
	private Integer maxPurchaseNum;
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
