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

    @NotNull
    private String address;
    @NotNull
    private String addressDetail;
    @NotNull
    private int postNum;
}
