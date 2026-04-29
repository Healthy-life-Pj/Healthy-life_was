package com.project.healthy_life_was.healthy_life.entity.order;

import com.project.healthy_life_was.healthy_life.entity.cart.Cart;
import com.project.healthy_life_was.healthy_life.entity.deliverAddress.DeliverAddress;
import com.project.healthy_life_was.healthy_life.entity.payment.Payment;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false, updatable = false)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_recipient_name", nullable = false)
    private String orderRecipientName;

    @Column(name = "order_recipient_phone", nullable = false)
    private String orderRecipientPhone;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = true)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "address_deliver_id", nullable = false)
    private DeliverAddress deliverAddress;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "order_total_amount", nullable = false)
    private int orderTotalAmount;

    @Column(name = "order_shipping_cost", nullable = false)
    @Builder.Default
    private int shippingCost = 3000;

    @Column(name = "order_shipping_request", nullable = false)
    private String shippingRequest;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @Column(name = "order_code", unique = true, length = 24)
    private String orderCode;

    @Column(name = "imp_uid", unique = true)
    private String impUid;
}