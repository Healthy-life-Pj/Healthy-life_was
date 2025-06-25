package com.project.healthy_life_was.healthy_life.dto.order.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderListResponseDto {
    private List<OrderResponseDto> orders;

    public OrderListResponseDto(List<OrderResponseDto> orders) {
        this.orders = orders;
    }
}
