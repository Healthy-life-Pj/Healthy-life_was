package com.project.healthy_life_was.healthy_life.entity.shipping;

import com.project.healthy_life_was.healthy_life.entity.order.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "Shipping")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long shippingId;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "shipping_tracking_num")
    private int shippingTrackingNum;

    @Column(name = "shipping_shipped_at")
    private LocalDateTime shippingShippedAt;

    @Column(name = "shipping_delivered_at")
    private LocalDateTime shippingDeliveredAt;

    @Column(name = "shipping_status")
    private ShippingStatus shippingStatus;
}
