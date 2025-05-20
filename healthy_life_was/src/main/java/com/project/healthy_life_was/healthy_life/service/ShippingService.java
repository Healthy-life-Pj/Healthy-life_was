package com.project.healthy_life_was.healthy_life.service;

import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.shipping.response.ShippingResponseDto;

import java.util.List;

public interface ShippingService {
    ResponseDto<ShippingResponseDto> createShipping(Long orderId);
    ResponseDto<ShippingResponseDto> findShippingByTrackingNumber(int trackingNum);
    ResponseDto<List<ShippingResponseDto>> getUserShipping(String username);
    ResponseDto<ShippingResponseDto> findShippingByOrderId(String username, Long orderId);
}
