package com.ll.gong9ri.boundedContext.groupBuyChatRoom.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.dto.GroupBuyChatRoomDto;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.entity.GroupBuyChatRoom;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.repository.GroupBuyChatRoomRepository;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.repository.GroupBuyChatRoomRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupBuyChatRoomService {

	private final GroupBuyChatRoomRepository groupBuyChatRoomRepository;
	private final GroupBuyChatRoomRepositoryImpl groupBuyChatRoomRepositoryImpl;

	@Transactional
	public GroupBuyChatRoom createChatRoom(GroupBuy groupBuy) {

		GroupBuyChatRoom chatRoom = GroupBuyChatRoom.builder()
			.groupBuy(groupBuy)
			.name(groupBuy.getName())
			.build();

		return groupBuyChatRoomRepository.save(chatRoom);
	}

	public GroupBuyChatRoom findById(Long chatRoomId) {
		return groupBuyChatRoomRepository.findById(chatRoomId).orElseThrow();
	}

	public Optional<GroupBuyChatRoom> findByGroupBuyId(Long groupBuyId) {
		return groupBuyChatRoomRepository.findByGroupBuyId(groupBuyId);
	}

	@Transactional
	public RsData<GroupBuyChatRoom> createNotice(GroupBuyChatRoom chatRoom, String notice) {
		GroupBuyChatRoom groupBuyChatRoom = chatRoom.toBuilder()
			.notice(notice)
			.build();

		groupBuyChatRoomRepository.save(groupBuyChatRoom);

		return RsData.of("S-1", "공지가 등록되었습니다.", groupBuyChatRoom);
	}

	public List<GroupBuyChatRoomDto> findAllByMemberId(Long memberId) {
		return groupBuyChatRoomRepositoryImpl.findAllByMemberId(memberId);
	}
}
