package com.ll.gong9ri.boundedContext.memberAttr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberMeHomeDTO {
	private String lastVisitDate;
	private Integer myPoint;
}
