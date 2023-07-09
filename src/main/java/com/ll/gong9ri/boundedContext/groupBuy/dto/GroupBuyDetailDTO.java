package com.ll.gong9ri.boundedContext.groupBuy.dto;

import java.time.LocalDateTime;

import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;

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
	private Boolean isParticipate;
}
