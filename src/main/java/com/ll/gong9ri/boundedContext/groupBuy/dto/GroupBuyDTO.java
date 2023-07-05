package com.ll.gong9ri.boundedContext.groupBuy.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GroupBuyDTO {
	private Long id;
	private String name;
	private Integer price;
	private String description;
	private Integer maxPurchaseNum;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private Integer currentMemberCount;

	public static GroupBuyDTO createGroupBuyDTO(GroupBuy groupBuy, Integer currentMemberCount){
		return GroupBuyDTO.builder()
			.id(groupBuy.getId())
			.name(groupBuy.getName())
			.price(groupBuy.getProduct().getPrice())
			.description(groupBuy.getProduct().getDescription())
			.maxPurchaseNum(groupBuy.getProduct().getMaxPurchaseNum())
			.startDate(groupBuy.getStartDate())
			.endDate(groupBuy.getEndDate())
			.currentMemberCount(currentMemberCount)
			.build();
	}

}
