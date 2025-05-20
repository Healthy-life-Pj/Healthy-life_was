package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT COALESCE(AVG(r.reviewRating), 0) FROM Review r WHERE r.orderDetail.product.pId = :pId")
    Double findAverageRatingByProductId(@Param("pId") Long pId);

    List<Review> findByUser_Username(String username);

    List<Review> findByOrderDetail_Product_pId(Long pId);

    Review findByUser_UsernameAndReviewId(String username, Long reviewId);

    boolean existsByUser_usernameAndOrderDetail_orderDetailId(String username, Long orderDetailId);
}
