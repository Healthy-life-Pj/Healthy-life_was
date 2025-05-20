package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.order.Order;
import com.project.healthy_life_was.healthy_life.entity.order.OrderDetail;
import com.project.healthy_life_was.healthy_life.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("""
    SELECT DISTINCT od
    FROM OrderDetail od
    WHERE od.order.user.username = :username
    AND (
        (:startOrderDate IS NULL OR od.order.orderDate >= :startOrderDate)
        AND (:endOrderDate IS NULL OR od.order.orderDate <= :endOrderDate)
    )
""")
    List<OrderDetail> findAllByUser_usernameAndStartAndEnd(
            @Param("username") String username,
            @Param("startOrderDate") LocalDate startOrderDate,
            @Param("endOrderDate") LocalDate endOrderDate
    );

    List<OrderDetail> findAllByOrder_User_Username(String username);
}
