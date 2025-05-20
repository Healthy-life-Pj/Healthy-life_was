package com.project.healthy_life_was.healthy_life.entity.whishList;

import com.project.healthy_life_was.healthy_life.entity.product.Product;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "wish_list_items")
@Data
public class WishListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_list_item_id")
    private Long wishListItemId;

    @ManyToOne
    @JoinColumn(name = "wish_list_id")
    private WishList wishList;

    @OneToOne
    @JoinColumn(name = "p_id")
    private Product product;

    @Column(name = "wish_list_added_at")
    private LocalDateTime wishListAddedAt;
} 