package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.physique.UserPhysiqueTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserPhysiqueTagRepository extends JpaRepository<UserPhysiqueTag, Long> {
    @Query(value = """
SELECT *
FROM USER_PHYSIQUE_TAGS
WHERE USER_ID = :userId
""", nativeQuery = true)
    Set<Long> findByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = """
DELETE
FROM USER_PHYSIQUE_TAGS
WHERE USER_ID = :userId
""", nativeQuery = true)
    void deleteAllByUserId(@Param("userId") Long userId);

    void deleteByUserPhysiqueTagId(UserPhysiqueTag.UserPhysiqueTagId id);
}
