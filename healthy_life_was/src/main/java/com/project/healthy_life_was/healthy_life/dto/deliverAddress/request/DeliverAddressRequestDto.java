package com.project.healthy_life_was.healthy_life.dto.deliverAddress.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliverAddressRequestDto {
    @NotNull
    private String address;
    @NotNull
    private String addressDetail;
    @NotNull
    private int postNum;
}
