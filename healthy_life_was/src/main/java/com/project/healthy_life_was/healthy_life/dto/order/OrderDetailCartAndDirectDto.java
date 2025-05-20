package com.project.healthy_life_was.healthy_life.dto.order;

import com.project.healthy_life_was.healthy_life.entity.order.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailCartAndDirectDto {

    private Long orderDetailId;
    private Long pId;
    private String pName;
    private int quantity;
    private int price;
    private int totalPrice;
    private LocalDate orderDate;

    public OrderDetailCartAndDirectDto(OrderDetail orderDetail) {
        this.orderDate =orderDetail.getOrder().getOrderDate();
        this.orderDetailId = orderDetail.getOrderDetailId();
        this.pId = orderDetail.getProduct().getPId();
        this.pName = orderDetail.getProduct().getPName();
        this.quantity = orderDetail.getQuantity();
        this.price = orderDetail.getPrice();
        this.totalPrice = orderDetail.getTotalPrice();
    }
}
