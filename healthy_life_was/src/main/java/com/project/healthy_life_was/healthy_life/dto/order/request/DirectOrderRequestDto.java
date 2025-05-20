package com.project.healthy_life_was.healthy_life.dto.order.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectOrderRequestDto {
    private String shippingRequest;
    @NotNull
    private int quantity;
}
