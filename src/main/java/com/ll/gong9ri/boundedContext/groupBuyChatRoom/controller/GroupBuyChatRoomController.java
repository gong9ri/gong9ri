package com.ll.gong9ri.boundedContext.groupBuyChatRoom.controller;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.gong9ri.base.event.EventAfterNoticeUpdated;
import com.ll.gong9ri.base.rq.Rq;
import com.ll.gong9ri.base.rsData.RsData;
import com.ll.gong9ri.boundedContext.chatRoomParticipants.service.ChatRoomParticipantService;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.dto.NoticeDto;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.entity.GroupBuyChatRoom;
import com.ll.gong9ri.boundedContext.groupBuyChatRoom.service.GroupBuyChatRoomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/groupbuy")
@RequiredArgsConstructor
public class GroupBuyChatRoomController {

	private final GroupBuyChatRoomService groupBuyChatRoomService;
	private final ChatRoomParticipantService chatRoomParticipantService;
	private final Rq rq;
	private final ApplicationEventPublisher publisher;

	@GetMapping("/{chatRoomId}")
	public String showChatRoom(@PathVariable Long chatRoomId, Model model) {

		GroupBuyChatRoom chatRoom = groupBuyChatRoomService.findById(chatRoomId);
		model.addAttribute("chatRoom", chatRoom);
		model.addAttribute("memberId", rq.getMember().getId());

		if (chatRoomParticipantService.findByMemberIdAndGroupBuyChatRoomId(rq.getMember().getId(), chatRoomId)
			.isEmpty()) {
			chatRoomParticipantService.createNewParticipant(chatRoom, rq.getMember());
		}

		return "groupBuy/roomDetail";
	}

	@PreAuthorize("isAuthenticated")
	@GetMapping("/{chatRoomId}/notice")
	public String getNoticeForm(@PathVariable Long chatRoomId) {
		return "groupBuy/noticeForm";
	}

	@PreAuthorize("isAuthenticated")
	@PostMapping("/{chatRoomId}/notice")
	public String createNotice(@PathVariable Long chatRoomId, @Valid NoticeDto dto) {
		GroupBuyChatRoom chatRoom = groupBuyChatRoomService.findById(chatRoomId);

		RsData<GroupBuyChatRoom> result = groupBuyChatRoomService.createNotice(chatRoom, dto.getNotice());

		List<String> tokensByChatRoomId = chatRoomParticipantService.getTokensByChatRoomId(chatRoomId);

		publisher.publishEvent(
			new EventAfterNoticeUpdated(tokensByChatRoomId, chatRoom.getName(), result.getData().getNotice()));

		return rq.redirectWithMsg("/groupbuy/{chatRoomId}", result);
	}
}
