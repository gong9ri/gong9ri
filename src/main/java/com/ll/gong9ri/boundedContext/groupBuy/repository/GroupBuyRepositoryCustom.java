package com.ll.gong9ri.boundedContext.groupBuy.repository;

import java.util.List;

import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyDetailDTO;
import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyListDTO;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;

public interface GroupBuyRepositoryCustom {
	List<GroupBuyListDTO> searchGroupBuyListDTO(
		final GroupBuyStatus groupBuyStatus,
		final Long memberId
	);

	GroupBuyDetailDTO getGroupBuyDetailDTO(final Long id, final Long memberId);
}
