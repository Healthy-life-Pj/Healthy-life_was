package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.cart.Cart;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser_Username(String username);
    Optional<Cart> findByUser(User user);

    @Modifying
    @Query("""
    DELETE FROM CartItem c WHERE c.cartItemId IN :cartItemIds
""")
    void deleteByCartItemIds(@Param("cartItemIds") List<Long> cartItemIds);
}
