package com.ll.gong9ri.boundedContext.groupBuyChatMessage.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.ll.gong9ri.boundedContext.groupBuyChatMessage.entity.GroupBuyChatMessage;

@Repository
public interface GroupBuyChatMessageRepository extends MongoRepository<GroupBuyChatMessage, String> {
	List<GroupBuyChatMessage> findByChatRoomId(String roomId);

	@Query(value = "{ 'chatRoomId': ?0, '_id': { $gt: ?1 } }")
	List<GroupBuyChatMessage> findNewChatMessages(String roomId, ObjectId lastObjectId);
}
