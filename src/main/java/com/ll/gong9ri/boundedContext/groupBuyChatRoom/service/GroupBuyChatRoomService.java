package com.ll.gong9ri.boundedContext.groupBuyChatRoom.service;

import org.springframework.stereotype.Service;

import com.ll.gong9ri.boundedContext.groupBuyChatRoom.repository.GroupBuyChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupBuyChatRoomService {

	private final GroupBuyChatRoomRepository groupBuyChatRoomRepository;
}
