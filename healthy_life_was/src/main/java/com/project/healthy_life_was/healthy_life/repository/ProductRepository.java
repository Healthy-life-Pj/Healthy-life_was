package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAll();

    @Query(value = """
    SELECT p
    FROM Product p
    JOIN p.productCategoryDetail.productCategory pc
    WHERE pc.pCategoryName = :pCategoryName
""")
    List<Product> findByPCategoryName(@Param("pCategoryName") String pCategoryName);

    @Query("""
        SELECT p
        FROM Product p
        JOIN p.productCategoryDetail pcd
        JOIN pcd.productCategory pc
        WHERE pcd.pCategoryDetailName = :pCategoryDetailName
        AND pc.pCategoryName = :pCategoryName
    """)
    List<Product> findByPCategoryNameAndPCategoryDetailsName(@Param("pCategoryName") String pCategoryName, @Param("pCategoryDetailName")String pCategoryDetailName);

    @Query("""
    SELECT p
    FROM Product p
    WHERE p.pName Like CONCAT('%', :pName, '%')
    """)
    List<Product> findByPName(@Param("pName") String pName);

    @Query(value = """
    SELECT DISTINCT p.*
    FROM products p
    JOIN physique_tags pt ON p.p_id = pt.p_id
    JOIN user_physique_tags upt ON pt.physique_tag_id = upt.physique_tag_id
    JOIN users u ON upt.user_id = u.user_id
    WHERE u.user_name = :username
""", nativeQuery = true)
    List<Product> findByUsername(@Param("username") String username);

    @Query("""
    SELECT count(p)
    FROM Product p
    WHERE p.pName = :pName
    """)
    boolean existsByPName(@Param("pName") String pName);

}
