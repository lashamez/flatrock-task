package com.flatrock.common.model.event;

import com.flatrock.common.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderStatusEvent {
    private long orderId;
    private long userId;
    private OrderStatus orderStatus;
    private Double totalPrice;
}
