package com.project.healthy_life_was.healthy_life.dto.deliverAddress.response;

import com.project.healthy_life_was.healthy_life.dto.deliverAddress.DeliverAddressDto;
import com.project.healthy_life_was.healthy_life.entity.deliverAddress.DeliverAddress;

import java.util.List;

public class DeliverAddressResponseDto {
    List<DeliverAddressDto> deliverAddressList;

    public DeliverAddressResponseDto(DeliverAddress deliverAddress) {
        this.deliverAddressList  = List.of(new DeliverAddressDto(deliverAddress));;
    }
}
