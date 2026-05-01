package com.project.healthy_life_was.healthy_life.dto.payment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KGPaymentDto {
    @NotBlank
    private String impUid;
    private String merchantUid;
}
