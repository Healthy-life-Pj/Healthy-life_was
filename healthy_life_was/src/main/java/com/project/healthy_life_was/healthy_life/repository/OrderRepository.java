package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
    SELECT DISTINCT o FROM Order o
    JOIN FETCH o.orderDetails od
    WHERE o.user.username = :username
    ORDER BY o.orderId DESC
""")
    List<Order> findAllByUser_Username(String username);

    @Query("""
    SELECT DISTINCT o
    FROM Order o
    JOIN FETCH o.orderDetails od
    WHERE o.user.username = :username
    AND (
        (:startOrderDate IS NULL OR o.orderDate >= :startOrderDate)
        AND (:endOrderDate IS NULL OR o.orderDate <= :endOrderDate)
    )
    ORDER BY o.orderDate DESC
""")
    List<Order> findAllByUser_usernameAndStartAndEnd(String username, LocalDateTime startOrderDate, LocalDateTime endOrderDate);

    List<Order> findAllByImpUid(String impUid);

    @Query("""
    SELECT o
    FROM Order o
    JOIN FETCH o.orderDetails od
    WHERE o.user.username = :username
    AND od.orderStatus = "DELIVERED"
""")
    List<Order> findByUser_Username(String username);
}
