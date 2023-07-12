package com.ll.gong9ri.boundedContext.order.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "OrderLog")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderLog {
	@Id
	private String id;
	@Builder.Default
	private LocalDateTime createDate = LocalDateTime.now();
	private String orderId;
	private String name;
	private Long memberId;
	private String username;
	private Long storeId;
	private String storeName;
	private Long productId;
	private String productName;
	private Integer price;
	private Integer salePrice;
	@Builder.Default
	private OrderStatus orderStatus = OrderStatus.PRE_CREATE;
	private String recipient;
	private String phoneNumber;
	private String mainAddress;
	private String subAddress;
	private Integer totalPrice;
	@Builder.Default
	private List<ProductOptionQuantity> productOptionQuantities = new ArrayList<>();
	private String paymentKey;

	public OrderLog newLogOf() {
		return this.toBuilder().id(null).build();
	}
}
