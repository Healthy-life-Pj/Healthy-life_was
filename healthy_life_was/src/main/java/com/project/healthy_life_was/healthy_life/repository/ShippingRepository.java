package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.shipping.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {

    @Query(value = """
SELECT s.*
FROM SHIPPING s
WHERE s.SHIPPING_TRACKING_NUM = :trackingNum
""", nativeQuery = true)
    Optional<Shipping> findByTrackingNum(@Param("trackingNum") int trackingNum);

    Optional<Shipping> findByOrderOrderId(Long orderId);

    @Query(value = """
SELECT s.*
FROM SHIPPING s
JOIN ORDERS o ON s.ORDER_ID = o.ORDER_ID
WHERE o.USER_ID = :userId
""", nativeQuery = true)
    List<Shipping> findByUserId(@Param("userId") Long userId);
}
