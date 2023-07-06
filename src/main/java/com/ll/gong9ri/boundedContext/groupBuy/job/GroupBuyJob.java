package com.ll.gong9ri.boundedContext.groupBuy.job;

import java.time.Instant;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuy;
import com.ll.gong9ri.boundedContext.groupBuy.entity.GroupBuyStatus;
import com.ll.gong9ri.boundedContext.groupBuy.service.GroupBuyService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GroupBuyJob {
	private final TaskScheduler taskScheduler;
	private final GroupBuyService groupBuyService;

	public void scheduleExpiredCheck(final GroupBuy groupBuy) {
		// TODO: cancel Task
		Runnable task = () -> groupBuyService.updateStatus(groupBuy, GroupBuyStatus.EXPIRE);
		taskScheduler.schedule(task, Instant.from(groupBuy.getEndDate()));
	}
}
