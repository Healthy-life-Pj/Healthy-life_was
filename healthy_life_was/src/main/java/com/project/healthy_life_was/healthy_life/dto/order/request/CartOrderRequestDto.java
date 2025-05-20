package com.project.healthy_life_was.healthy_life.dto.order.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartOrderRequestDto {
    private List<Long> cartItemIds = new ArrayList<>();
    private String shippingRequest;
}
