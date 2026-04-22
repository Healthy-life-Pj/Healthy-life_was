package com.project.healthy_life_was.healthy_life.service;

import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.deliverAddress.request.DeliverAddressRequestDto;
import com.project.healthy_life_was.healthy_life.dto.deliverAddress.response.DeliverAddressListResponseDto;
import com.project.healthy_life_was.healthy_life.dto.deliverAddress.response.DeliverAddressResponseDto;

public interface DeliverAddressService {
    ResponseDto<DeliverAddressResponseDto> createAddress(String username, DeliverAddressRequestDto dto);

    ResponseDto<DeliverAddressListResponseDto> getAddressAll(String username);

    ResponseDto<DeliverAddressResponseDto> updateAddress(String username, DeliverAddressRequestDto dto, Long deliverAddressId);

    ResponseDto<Object> deleteAddress(String username, Long deliverAddressId);

    ResponseDto<DeliverAddressResponseDto> getAddressOne(String username, Long deliverAddressId);

    ResponseDto<DeliverAddressResponseDto> addressIsDefault(String username, Long deliverAddressId);
}
