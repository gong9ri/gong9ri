package com.ll.gong9ri.boundedContext.groupBuyChatRoom.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyChatRoomDto {
	private Long groupBuyChatRoomId;
	private String name;
	private LocalDateTime endDate;
}
