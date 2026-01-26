package com.project.healthy_life_was.healthy_life.dto.order.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.healthy_life_was.healthy_life.entity.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderReviewResponseDto {
    private Long orderDetailId;
    @JsonProperty("pId")
    private Long pId;
    @JsonProperty("pName")
    private String pName;
    @JsonProperty("pImgUrl")
    private String pImgUrl;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;
}
