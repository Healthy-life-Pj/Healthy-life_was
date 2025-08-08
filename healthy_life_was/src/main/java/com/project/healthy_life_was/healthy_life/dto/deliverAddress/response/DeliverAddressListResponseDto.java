package com.project.healthy_life_was.healthy_life.dto.deliverAddress.response;

import com.project.healthy_life_was.healthy_life.dto.deliverAddress.DeliverAddressDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
public class DeliverAddressListResponseDto {
    List<DeliverAddressDto> deliverAddressDto;

    public DeliverAddressListResponseDto(List<DeliverAddressDto> response) {
        this.deliverAddressDto = response;
    }
}
