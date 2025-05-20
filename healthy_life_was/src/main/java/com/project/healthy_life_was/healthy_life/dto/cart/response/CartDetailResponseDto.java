package com.project.healthy_life_was.healthy_life.dto.cart.response;

import com.project.healthy_life_was.healthy_life.dto.cart.CartItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailResponseDto {

    private List<CartItemDto> cartItem;
}
