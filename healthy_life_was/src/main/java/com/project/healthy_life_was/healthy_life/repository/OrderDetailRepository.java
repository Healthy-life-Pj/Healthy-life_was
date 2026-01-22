package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.dto.order.request.OrderReviewRequestDto;
import com.project.healthy_life_was.healthy_life.entity.order.Order;
import com.project.healthy_life_was.healthy_life.entity.order.OrderDetail;
import com.project.healthy_life_was.healthy_life.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query("""
    SELECT DISTINCT od From OrderDetail od
    WHERE od.orderDetailId IN :orderDetailIds
""")
    List<OrderDetail> findByOrderDetailIds(@Param("orderDetailIds") List<Long> orderDetailIds);

    @Query("""
    SELECT DISTINCT o
    FROM Order o
    JOIN o.orderDetails od
    WHERE o.user.username = :username
      AND od.orderStatus = 'DELIVERED'
      AND o.orderDate >= :limitDate
      AND NOT EXISTS (
          SELECT 1
          FROM Review r
          WHERE r.orderDetail.orderDetailId = od.orderDetailId
      )
""")
    List<Order> findCanCreateReviewOrders(
            @Param("username") String username,
            @Param("limitDate") LocalDateTime limitDate
    );


}
