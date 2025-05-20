package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAll();

    @Query(value = """
    SELECT p.*
    FROM products p 
    JOIN product_category_details pcd ON pcd.p_id = p.p_id
    JOIN product_category pc ON pcd.p_category_id = pc.p_category_id
    WHERE pc.p_category_name = :pCategoryName
""", nativeQuery = true)
    List<Product> findByPCategoryName(@Param("pCategoryName") String pCategoryName);

    @Query(value = """
        SELECT p.*
        FROM products p
        JOIN product_category_details pcd ON p.p_id = pcd.p_id
        WHERE pcd.p_category_details_name = :pCategoryDetailName
    """, nativeQuery = true)
    List<Product> findByPCategoryDetailsName(@Param("pCategoryDetailName")String pCategoryDetailName);

    @Query("""
    SELECT p
    FROM Product p
    WHERE p.pName Like CONCAT('%', :pName, '%')
    """)
    List<Product> findByPName(@Param("pName") String pName);

    @Query(value = """
    SELECT DISTINCT p.* 
    FROM products p
    JOIN physique_tag pt ON p.p_id = pt.p_id
    JOIN user_physique_tag upt ON pt.physique_tag_id = upt.physique_tag_id
    JOIN users u ON upt.user_id = u.user_id
    WHERE u.user_name = :username
""", nativeQuery = true)
    List<Product> findByUsername(@Param("username") String username);
}
