package com.project.healthy_life_was.healthy_life.entity.order;

import com.project.healthy_life_was.healthy_life.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_details")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id", nullable = false, updatable = false)
    private Long orderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "p_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name ="total_price", nullable = false)
    private int totalPrice;

}