package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.physique.PhysiqueTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PhysiqueTagRepository extends JpaRepository<PhysiqueTag, Long> {

    Set<PhysiqueTag> findAllByPhysiqueName(String tag);

    @Query("""
    SELECT DISTINCT pt.physiqueName
    FROM PhysiqueTag pt
""")
    Set<String> findAllTag();
}
