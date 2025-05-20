package com.project.healthy_life_was.healthy_life.entity.order;

import com.project.healthy_life_was.healthy_life.entity.cart.Cart;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = true)
    private Cart cart;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "order_total_amount", nullable = false)
    private int orderTotalAmount;

    @Column(name = "order_shipping_cost", nullable = false)
    @Builder.Default
    private int shippingCost = 3000;

    @Column(name = "order_shipping_request", nullable = false)
    private String shippingRequest;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

}