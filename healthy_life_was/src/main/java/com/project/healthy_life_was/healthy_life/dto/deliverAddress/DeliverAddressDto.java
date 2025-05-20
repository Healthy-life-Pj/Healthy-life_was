package com.project.healthy_life_was.healthy_life.dto.deliverAddress;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DeliverAddressDto {

    private String address;

    private String addressDetail;

    private int postNum;
}
