package com.project.healthy_life_was.healthy_life.service;

import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.CartOrderRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.DirectOrderRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.request.OrderDetailIdListRequestDto;
import com.project.healthy_life_was.healthy_life.dto.order.response.*;

import java.time.LocalDate;

public interface OrderService {
    ResponseDto<PostOrderResponseDto> cartOrder(String username, CartOrderRequestDto dto);

    ResponseDto<PostOrderResponseDto> directOrder(String username, Long pId, DirectOrderRequestDto dto);

    ResponseDto<OrderListResponseDto> getOrder(String username, LocalDate startOrderDate, LocalDate endOrderDate);

    ResponseDto<OrderListResponseDto> changeOrderStatus(String username, OrderDetailIdListRequestDto dto, String orderStatus);

    ResponseDto<OrderCancelResponseDto> cancelReturnOrExchange(String username, Long orderDetailId);

    ResponseDto<OrderListResponseDto> orderGetReview(String username);
}
