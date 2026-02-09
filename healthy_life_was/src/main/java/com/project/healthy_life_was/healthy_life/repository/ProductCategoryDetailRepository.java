package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.product.ProductCategory;
import com.project.healthy_life_was.healthy_life.entity.product.ProductCategoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductCategoryDetailRepository extends JpaRepository<ProductCategoryDetail, Long> {

    @Query("""
    SELECT pcd
    FROM ProductCategoryDetail pcd
    JOIN pcd.products p
    WHERE p.pId = :pId
""")
    ProductCategoryDetail findByPId(@Param("pId") Long pId);
}
