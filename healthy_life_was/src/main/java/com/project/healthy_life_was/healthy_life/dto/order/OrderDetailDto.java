package com.project.healthy_life_was.healthy_life.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.healthy_life_was.healthy_life.entity.order.OrderDetail;
import com.project.healthy_life_was.healthy_life.entity.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {
    private Long orderDetailId;
    @JsonProperty("pId")
    private Long pId;
    @JsonProperty("pName")
    private String pName;
    @JsonProperty("pImgUrl")
    private String pImgUrl;
    private int price;
    private int quantity;
    private int totalPrice;
    private OrderStatus orderStatus;

    public OrderDetailDto(OrderDetail orderDetail) {
        this.orderDetailId = orderDetail.getOrderDetailId();
        this.pId = orderDetail.getProduct().getPId();
        this.pName = orderDetail.getProduct().getPName();
        this.pImgUrl = orderDetail.getProduct().getPImgUrl();
        this.price = orderDetail.getPrice();
        this.quantity = orderDetail.getQuantity();
        this.totalPrice = orderDetail.getTotalPrice();
        this.orderStatus = orderDetail.getOrderStatus();
    }
}

