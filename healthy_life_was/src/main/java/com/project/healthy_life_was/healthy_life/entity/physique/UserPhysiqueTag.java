package com.project.healthy_life_was.healthy_life.entity.physique;

import com.project.healthy_life_was.healthy_life.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Table(name = "user_physique_tags")
public class UserPhysiqueTag {
    @EmbeddedId
    private UserPhysiqueTagId userPhysiqueTagId;

    @MapsId("userId")
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("physiqueTagId")
    @ManyToOne
    @JoinColumn(name = "physique_tag_id", nullable = false)
    private PhysiqueTag physiqueTag;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserPhysiqueTagId implements Serializable {
        private Long userId;
        private Long physiqueTagId;
    }
}
