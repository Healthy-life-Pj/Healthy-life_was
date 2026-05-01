package com.project.healthy_life_was.healthy_life.dto.order.request;

import com.project.healthy_life_was.healthy_life.dto.payment.KGPaymentDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectOrderRequestDto {
    @NotNull
    private int quantity;
    @NotNull
    private String orderRecipientName;
    @NotNull
    private String orderRecipientPhone;
    @NotNull
    private Long deliverAddressId;
    @NotNull
    private int shippingCost;
    private String shippingRequest;
    @NotNull
    private KGPaymentDto kgPayment;
}
