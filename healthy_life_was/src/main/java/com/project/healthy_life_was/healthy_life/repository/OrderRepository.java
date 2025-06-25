package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.order.Order;
import com.project.healthy_life_was.healthy_life.entity.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
    SELECT DISTINCT o FROM Order o
    JOIN FETCH o.orderDetails od
    WHERE o.user.username = :username
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
""")
    List<Order> findAllByUser_usernameAndStartAndEnd(String username, LocalDate startOrderDate, LocalDate endOrderDate);

    @Query("""
    SELECT o
    FROM Order o
    JOIN FETCH o.orderDetails od 
    WHERE o.orderStatus = 'DELIVERED' AND o.user.username = :username
        AND FUNCTION('DATEDIFF', CURRENT_DATE, o.orderDate) <= 30
        AND NOT EXISTS (
            SELECT 1 FROM Review r
            WHERE r.orderDetail.orderDetailId = od.orderDetailId
                AND r.user.username = :username
        )
""")
    List<Order> findDeliveredOrdersWithoutReview(String username);
}
