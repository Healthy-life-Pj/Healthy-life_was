package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.cart.Cart;
import com.project.healthy_life_was.healthy_life.entity.cart.CartItem;
import com.project.healthy_life_was.healthy_life.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart_User_Username(String username);
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    List<CartItem> findByCart(Cart cart);
}
