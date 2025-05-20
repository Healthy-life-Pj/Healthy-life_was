package com.project.healthy_life_was.healthy_life.entity.review;

import com.project.healthy_life_was.healthy_life.entity.order.OrderDetail;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reviews")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "order_detail_id", nullable = false)
    private OrderDetail orderDetail;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "review_rating", nullable = false)
    private Double reviewRating;

    @Column(name = "review_content", nullable = false)
    private String reviewContent;

    @Column(name = "review_create_at", nullable = false)
    private LocalDate reviewCreatAt;

    @Column(name = "review_img_url")
    private String reviewImgUrl;
}
