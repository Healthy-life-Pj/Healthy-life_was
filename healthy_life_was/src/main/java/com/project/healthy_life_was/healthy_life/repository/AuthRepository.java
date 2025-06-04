package com.project.healthy_life_was.healthy_life.repository;


import com.project.healthy_life_was.healthy_life.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
    boolean existsByUserNickName(String userNickName);
    User findByUsername(String username);

    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByNameAndUserEmail(String name, String userEmail);

    Optional<User> findBySnsIdAndJoinPath(String snsId, String joinPath);
    boolean existsBySnsIdAndJoinPath(String snsId, String joinPath);
}
