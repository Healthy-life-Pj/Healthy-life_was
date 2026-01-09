package com.project.healthy_life_was.healthy_life.dto.cart.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartSelectRequestDto {
    private List<Long> cartItemIds = new ArrayList<>();
}
