package com.ll.gong9ri.boundedContext.order.dto;

import com.ll.gong9ri.boundedContext.order.entity.OrderInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoListDTO {
	private Long id;
	private String name;
	private Long productId;
	private String productName;
	private Long storeId;
	private String storeName;
	private Integer price;
	private String orderStatus;

	public static OrderInfoListDTO of(final OrderInfo orderInfo) {
		return OrderInfoListDTO.builder()
			.id(orderInfo.getId())
			.name(orderInfo.getName())
			.productId(orderInfo.getProduct().getId())
			.productName(orderInfo.getProduct().getName())
			.storeId(orderInfo.getStore().getId())
			.storeName(orderInfo.getStore().getName())
			.price(orderInfo.getPrice())
			.orderStatus(orderInfo.getOrderStatus().getStatus())
			.build();
	}
}
