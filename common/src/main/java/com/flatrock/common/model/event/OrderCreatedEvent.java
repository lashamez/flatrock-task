package com.flatrock.common.model.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flatrock.common.model.OrderItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderCreatedEvent {
    private long orderId;
    private Long customerId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private List<OrderItemDto> items;
    private double totalPrice;
}
