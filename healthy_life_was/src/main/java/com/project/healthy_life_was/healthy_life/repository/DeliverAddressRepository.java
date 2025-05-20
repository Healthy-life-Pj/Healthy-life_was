package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.deliverAddress.DeliverAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliverAddressRepository extends JpaRepository<DeliverAddress, Long> {
    List<DeliverAddress> findByUser_UserId(Long userId);
}
