package com.ll.gong9ri.boundedContext.groupBuyChatMessage.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "messages")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class GroupBuyChatMessage {

	@Id
	private String id;

	private String chatRoomId;
	private String senderId;

	private String content;
	private Boolean isSeller;

	private LocalDateTime createDate;
}
