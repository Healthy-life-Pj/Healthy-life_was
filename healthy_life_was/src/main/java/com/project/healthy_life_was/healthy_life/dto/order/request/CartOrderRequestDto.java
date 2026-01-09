package com.project.healthy_life_was.healthy_life.dto.order.request;

import com.project.healthy_life_was.healthy_life.dto.payment.KGPaymentDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartOrderRequestDto {
    @NotNull
    private List<Long> cartItemIds = new ArrayList<>();
    @NotNull
    private String orderRecipientName;
    @NotNull
    private String orderRecipientPhone;
    @NotNull
    private Long deliverAddressId;
    private String shippingRequest;
    @NotNull
    private KGPaymentDto kgPayment;
    @NotNull
    private int shippingCost;
}
