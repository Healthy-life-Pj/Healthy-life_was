package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_Username(String username);
}
