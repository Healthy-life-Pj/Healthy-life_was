package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.cart.Cart;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser_Username(String username);
    Optional<Cart> findByUser_UsernameAndCartId(String username, Long cartId);
    Optional<Cart> findByUser(User user);
}
