package com.project.healthy_life_was.healthy_life.dto.order.response;

import com.project.healthy_life_was.healthy_life.dto.order.OrderDetailDto;
import com.project.healthy_life_was.healthy_life.entity.order.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponseDto {
    private Set<OrderDetailDto> orderDetailsDto;

    public OrderDetailResponseDto(List<OrderDetail> orderDetails) {
        this.orderDetailsDto = orderDetails.stream()
                .map(OrderDetailDto::new)
                .collect(Collectors.toSet());
    }
}
