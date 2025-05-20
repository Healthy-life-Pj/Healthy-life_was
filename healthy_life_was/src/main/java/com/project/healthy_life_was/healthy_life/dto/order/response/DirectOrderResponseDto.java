package com.project.healthy_life_was.healthy_life.dto.order.response;

import com.project.healthy_life_was.healthy_life.dto.order.OrderDetailCartAndDirectDto;
import com.project.healthy_life_was.healthy_life.entity.order.Order;
import com.project.healthy_life_was.healthy_life.entity.order.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectOrderResponseDto {

    private Long orderId;
    private String username;
    private int totalAmount;
    private String shippingRequest;
    private String orderStatus;
    private List<OrderDetailCartAndDirectDto> orderDetails;
    private LocalDate orderDate;

    public DirectOrderResponseDto(Order order, List<OrderDetail> orderDetails) {
        this.orderId = order.getOrderId();
        this.username = order.getUser().getUsername();
        this.totalAmount = order.getOrderTotalAmount();
        this.shippingRequest = order.getShippingRequest();
        this.orderStatus = order.getOrderStatus().name();
        this.orderDetails = orderDetails.stream()
                .map(OrderDetailCartAndDirectDto::new)  // `OrderDetailDto`로 변환
                .collect(Collectors.toList());
    }
}
