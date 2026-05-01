package com.project.healthy_life_was.healthy_life.dto.deliverAddress.response;

import com.project.healthy_life_was.healthy_life.dto.deliverAddress.DeliverAddressDto;
import com.project.healthy_life_was.healthy_life.entity.deliverAddress.DeliverAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
public class DeliverAddressResponseDto {
    DeliverAddressDto deliverAddressDto;

    public DeliverAddressResponseDto(DeliverAddressDto response) {
        this.deliverAddressDto = response;
    }
}
