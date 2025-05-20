package com.project.healthy_life_was.healthy_life.dto.shipping.response;

import com.project.healthy_life_was.healthy_life.entity.shipping.Shipping;
import com.project.healthy_life_was.healthy_life.entity.shipping.ShippingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingResponseDto {
    private Long orderId;
    private Long shippingId;
    private int shippingTrackingNum;
    private LocalDateTime shippingShippedAt;
    private LocalDateTime shippingDeliveredAt;
    private ShippingStatus shippingStatus;

    public ShippingResponseDto(Shipping shipping) {
        this.orderId = shipping.getOrder().getOrderId();
        this.shippingId = shipping.getShippingId();
        this.shippingTrackingNum = shipping.getShippingTrackingNum();
        this.shippingShippedAt = shipping.getShippingShippedAt();
        this.shippingDeliveredAt = shipping.getShippingDeliveredAt();
        this.shippingStatus = ShippingStatus.PENDING;
    }
}
