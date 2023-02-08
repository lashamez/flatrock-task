package com.flatrock.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SellerItemData {
    private Long sellerId;
    private List<OrderItemDto> orderItems;
    private OrderStatus orderStatus;
    public SellerItemData(Long sellerId, List<OrderItemDto> orderItems) {
        this.sellerId = sellerId;
        this.orderItems = orderItems;
    }

}
