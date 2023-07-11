package com.ll.gong9ri.boundedContext.groupBuy.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyDetailDTO;
import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyListDTO;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;

public interface GroupBuyRepositoryCustom {
	Page<GroupBuyListDTO> searchGroupBuyListDTO(
		final GroupBuyStatus status,
		final Long memberId,
		Pageable pageable);

	GroupBuyDetailDTO getGroupBuyDetailDTO(final Long id, final Long memberId);
}
