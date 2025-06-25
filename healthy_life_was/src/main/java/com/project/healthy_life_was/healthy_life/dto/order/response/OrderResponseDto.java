package com.project.healthy_life_was.healthy_life.dto.order.response;

import com.project.healthy_life_was.healthy_life.dto.order.OrderDetailDto;
import com.project.healthy_life_was.healthy_life.entity.order.Order;
import com.project.healthy_life_was.healthy_life.entity.order.OrderDetail;
import com.project.healthy_life_was.healthy_life.entity.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private String username;
    private String shippingRequest;
    private int shippingCost = 3000;
    private Integer totalAmount;
    private OrderStatus orderStatus;
    private LocalDate orderDate;
    private List<OrderDetailDto> orderDetails;

    public OrderResponseDto(Order order) {
        this.orderId = order.getOrderId();
        this.username = order.getUser().getUsername();
        this.shippingRequest = order.getShippingRequest();
        this.totalAmount = order.getOrderTotalAmount();
        this.orderStatus = order.getOrderStatus();
        this.orderDate = order.getOrderDate();

        this.orderDetails = order.getOrderDetails().stream()
                .map(OrderDetailDto::new)
                .collect(Collectors.toList());
    }
}
