package com.ll.gong9ri.boundedContext.groupBuyChatRoom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ll.gong9ri.boundedContext.groupBuyChatRoom.entity.GroupBuyChatRoom;

@Repository
public interface GroupBuyChatRoomRepository extends JpaRepository<GroupBuyChatRoom, Long> {
}
