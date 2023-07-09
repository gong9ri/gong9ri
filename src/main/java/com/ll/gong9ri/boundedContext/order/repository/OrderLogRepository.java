package com.ll.gong9ri.boundedContext.order.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ll.gong9ri.boundedContext.order.entity.OrderLog;

@Repository
public interface OrderLogRepository extends MongoRepository<OrderLog, String> {
	List<OrderLog> findAllByOrderId(final String orderId);
}
