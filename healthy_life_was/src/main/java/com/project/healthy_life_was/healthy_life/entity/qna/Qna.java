package com.project.healthy_life_was.healthy_life.entity.qna;

import com.project.healthy_life_was.healthy_life.entity.product.Product;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "qnas")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Qna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id", nullable = false, updatable = false)
    private Long qnaId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "p_id", nullable = false)
    private Product product;

    @Column(name = "qna_title", nullable = false)
    private String qnaTitle;

    @Column(name = "qna_content", nullable = false)
    private String qnaContent;

    @Column(name = "qna_answer", nullable = false)
    private String qnaAnswer;
}
