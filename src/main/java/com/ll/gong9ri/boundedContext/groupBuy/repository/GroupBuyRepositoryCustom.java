package com.ll.gong9ri.boundedContext.groupBuy.repository;

import java.util.List;

import com.ll.gong9ri.boundedContext.groupBuy.dto.GroupBuyDTO;

public interface GroupBuyRepositoryCustom {
	List<GroupBuyDTO> findAllProgressGroupBuyMemberCount();

	List<GroupBuyDTO> findAllGroupBuyMemberCount();
}
