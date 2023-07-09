package com.ll.gong9ri.boundedContext.groupBuy.dto;

import java.time.LocalDateTime;

import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;

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
	private Boolean isParticipate;
}
