package com.project.healthy_life_was.healthy_life.entity.physique;

import com.project.healthy_life_was.healthy_life.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "physique_tags")
public class PhysiqueTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "physique_tag_id", nullable = false, unique = true)
    private Long physiqueTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_id", nullable = true)
    private Product product;

    @Column(name = "physique_tag_name", nullable = false, unique = true)
    private String physiqueName;
}
