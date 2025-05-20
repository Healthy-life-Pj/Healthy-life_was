package com.project.healthy_life_was.healthy_life.service;

import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.cart.request.CartAddRequestDto;
import com.project.healthy_life_was.healthy_life.dto.cart.request.CartUpdateQuantityRequestDto;
import com.project.healthy_life_was.healthy_life.dto.cart.request.DeleteCartItemsDto;
import com.project.healthy_life_was.healthy_life.dto.cart.response.CartAddResponseDto;
import com.project.healthy_life_was.healthy_life.dto.cart.response.CartDetailResponseDto;
import com.project.healthy_life_was.healthy_life.dto.cart.response.CartUpdateResponseDto;

public interface CartService {
    ResponseDto<CartAddResponseDto> createCart(String username, Long pId, CartAddRequestDto dto);
    ResponseDto<CartDetailResponseDto> getCartUser(String username);
    ResponseDto<CartUpdateResponseDto> updateCart(String username, Long cartItemId, CartUpdateQuantityRequestDto dto);
    ResponseDto<Object> deleteCartItemIds(String username, DeleteCartItemsDto dto);
    ResponseDto<Object> deleteCartAll(String username);
}
