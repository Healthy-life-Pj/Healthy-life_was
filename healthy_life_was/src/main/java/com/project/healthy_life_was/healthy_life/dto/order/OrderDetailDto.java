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

    public OrderDetailDto(OrderDetail orderDetail) {
        this.orderDetailId = orderDetail.getOrderDetailId();
        this.pId = orderDetail.getProduct().getPId();
        this.pName = orderDetail.getProduct().getPName();
        this.quantity = orderDetail.getQuantity();
        this.price = orderDetail.getPrice();
        this.totalPrice = orderDetail.getTotalPrice();
    }

}
