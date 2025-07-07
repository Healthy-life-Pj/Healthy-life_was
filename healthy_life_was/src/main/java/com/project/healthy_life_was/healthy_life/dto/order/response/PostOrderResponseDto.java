package com.project.healthy_life_was.healthy_life.dto.order.response;

import com.project.healthy_life_was.healthy_life.dto.order.OrderDetailDto;
import com.project.healthy_life_was.healthy_life.entity.order.Order;
import com.project.healthy_life_was.healthy_life.entity.order.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
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
    private LocalDate orderDate;
//    private String paymentMethod;
//    private String paymentTid;

    public PostOrderResponseDto(Order order, List<OrderDetail> orderDetails) {
        this.orderId = order.getOrderId();
        this.orderRecipientName = order.getOrderRecipientName();
        this.orderRecipientPhone = order.getOrderRecipientPhone();
        this.totalAmount = order.getOrderTotalAmount();
        this.shippingRequest = order.getShippingRequest();
        this.shippingCost = 3000; // 혹은 order.getShippingCost()로 변경 가능
        this.orderDate = order.getOrderDate();
//        this.paymentMethod = order.getPaymentMethod();
//        this.paymentTid = order.getPaymentTid();
        this.orderDetails = orderDetails.stream()
                .map(OrderDetailDto::new)
                .collect(Collectors.toList());
    }

}
