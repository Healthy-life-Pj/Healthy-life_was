package com.project.healthy_life_was.healthy_life.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.healthy_life_was.healthy_life.entity.order.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {

    private Long orderId;
    private String username;
    private Integer totalAmount;
    private String shippingRequest;
    private String orderStatus;
    private Long orderDetailId;
    @JsonProperty("pId")
    private Long pId;
    @JsonProperty("pName")
    private String pName;
    @JsonProperty("pImgUrl")
    private String pImgUrl;
    private int quantity;
    private int price;
    private int totalPrice;
    private LocalDate orderDate;

    public OrderDetailDto(OrderDetail orderDetail) {
        this.orderId = orderDetail.getOrder().getOrderId();
        this.username = orderDetail.getOrder().getUser().getUsername();
        this.totalAmount = orderDetail.getOrder().getOrderTotalAmount();
        this.shippingRequest = orderDetail.getOrder().getShippingRequest();
        this.orderStatus = String.valueOf(orderDetail.getOrder().getOrderStatus());
        this.orderDate =orderDetail.getOrder().getOrderDate();
        this.orderDetailId = orderDetail.getOrderDetailId();
        this.pId = orderDetail.getProduct().getPId();
        this.pName = orderDetail.getProduct().getPName();
        this.quantity = orderDetail.getQuantity();
        this.price = orderDetail.getPrice();
        this.totalPrice = orderDetail.getTotalPrice();
    }

}
