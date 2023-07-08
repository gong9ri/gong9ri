package com.ll.gong9ri.boundedContext.groupBuy.repository;

import java.util.List;

import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyDetailDTO;
import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyListDTO;
import com.ll.gong9ri.boundedContext.member.entity.Member;

public interface GroupBuyRepositoryCustom {
	List<GroupBuyListDTO> getAllGroupBuyListDTOInProgress();

	List<GroupBuyListDTO> getAllGroupBuyListDTO();

	GroupBuyDetailDTO getGroupBuyDetailDTO(Member member);
}
