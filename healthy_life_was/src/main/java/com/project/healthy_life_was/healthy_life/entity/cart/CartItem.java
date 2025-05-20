package com.project.healthy_life_was.healthy_life.entity.cart;

import com.project.healthy_life_was.healthy_life.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id", nullable = false, updatable = false)
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "p_id", nullable = false)
    private Product product;

    @Column(name = "product_quantity", nullable =false)
    @Builder.Default
    private int productQuantity = 1;

    @Column(name = "product_price", nullable = false)
    private int productPrice;
}
