package com.project.healthy_life_was.healthy_life.dto.order.response;

import com.project.healthy_life_was.healthy_life.dto.order.OrderDetailDto;
import com.project.healthy_life_was.healthy_life.entity.order.Order;
import com.project.healthy_life_was.healthy_life.entity.order.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class PostOrderResponseDto {
    private Long orderId;
    private String orderRecipientName;
    private String orderRecipientPhone;
    private int totalAmount;
    private String shippingRequest;
    private int shippingCost = 3000;
    private List<OrderDetailDto> orderDetails;
    private LocalDateTime orderDate;
    private String orderCode;
    private String impUid;


    public PostOrderResponseDto(Order order, List<OrderDetail> orderDetails) {
        this.orderId = order.getOrderId();
        this.orderRecipientName = order.getOrderRecipientName();
        this.orderRecipientPhone = order.getOrderRecipientPhone();
        this.totalAmount = order.getOrderTotalAmount();
        this.shippingRequest = order.getShippingRequest();
        this.shippingCost = order.getShippingCost();
        this.orderDate = order.getOrderDate();
        this.orderCode = order.getOrderCode();
        this.impUid = order.getImpUid();
        this.orderDetails = orderDetails.stream()
                .map(OrderDetailDto::new)
                .collect(Collectors.toList());
    }
}
