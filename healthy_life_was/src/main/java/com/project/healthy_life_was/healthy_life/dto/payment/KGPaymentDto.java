package com.project.healthy_life_was.healthy_life.dto.payment;

import com.project.healthy_life_was.healthy_life.entity.payment.PaymentMethod;
import com.project.healthy_life_was.healthy_life.entity.payment.PaymentStatus;
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
