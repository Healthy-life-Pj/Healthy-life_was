package com.project.healthy_life_was.healthy_life.dto.order;

import com.project.healthy_life_was.healthy_life.entity.deliverAddress.DeliverAddress;
import com.project.healthy_life_was.healthy_life.entity.order.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long orderId;
    private String orderRecipientName;
    private String orderRecipientPhone;
    private String shippingRequest;
    private DeliverAddress deliverAddress;
    private int shippingCost = 3000;
    private Integer totalAmount;
    private LocalDateTime orderDate;
    private List<OrderDetailDto> orderDetails;
    private String orderCode;
    private String impUid;

    public OrderDto(Order order) {
        this.orderId = order.getOrderId();
        this.orderRecipientName = order.getOrderRecipientName();
        this.orderRecipientPhone = order.getOrderRecipientPhone();
        this.shippingRequest = order.getShippingRequest();
        this.totalAmount = order.getOrderTotalAmount();
        this.orderDate = order.getOrderDate();
        this.impUid = order.getImpUid();
        this.orderCode = order.getOrderCode();
        this.orderDetails = order.getOrderDetails().stream()
                .map(OrderDetailDto::new)
                .collect(Collectors.toList());
        this.deliverAddress = order.getDeliverAddress();
    }
}
