package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.physique.TagType;
import com.project.healthy_life_was.healthy_life.entity.physique.UserPhysiqueTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserPhysiqueTagRepository extends JpaRepository<UserPhysiqueTag, Long> {

    @Query("""
    SELECT upt.physiqueTag.physiqueTagId
    FROM UserPhysiqueTag upt
    JOIN upt.physiqueTag pt
    WHERE upt.user.userId = :userId
    AND pt.tagType = :includeType
    AND (
        :excludeType IS NULL
        OR pt.product.pId NOT IN (
            SELECT pt2.product.pId
            FROM PhysiqueTag pt2
            WHERE pt2.tagType = :excludeType
        )
)
""")
    Set<Long> findByUserIdAndTagType(
            @Param("userId") Long userId,
            @Param("includeType") TagType includeType,
            @Param("excludeType") TagType excludeType
    );

    void deleteByUserPhysiqueTagId(UserPhysiqueTag.UserPhysiqueTagId id);;

    @Query("""
    SELECT upt.physiqueTag.physiqueTagId
    FROM UserPhysiqueTag upt
    JOIN upt.physiqueTag pt
    WHERE upt.user.userId = :userId
""")
    Set<Long> findPhysiqueIdByUser_UserId(@Param("userId")Long userId);

    @Query("""
    SELECT DISTINCT upt.physiqueTag.physiqueName
    FROM UserPhysiqueTag upt
    JOIN upt.physiqueTag pt
    WHERE upt.user.userId = :userId
    """)
    Set<String> findPhysiqueTagNameByUser_UserId(Long userId);

    List<UserPhysiqueTag> findAllByUser_UserId(Long userId);

    @Query("""
    SELECT upt.physiqueTag.physiqueTagId
    FROM UserPhysiqueTag upt
    WHERE upt.user.username = :username
    """)
    List<Long> findByUserName(@Param("username") String username);
}
