package com.project.healthy_life_was.healthy_life.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.healthy_life_was.healthy_life.entity.deliverAddress.DeliverAddress;
import com.project.healthy_life_was.healthy_life.entity.whishList.WishList;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    @Id
    @Column(name = "user_id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "user_password", nullable = false)
    @JsonIgnoreProperties
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "user_nickname", nullable = false)
    private String userNickName;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @Column(name = "user_birth", nullable = false)
    private Date userBirth;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_phone", nullable = false)
    private String userPhone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender userGender;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberShip userMemberGrade = MemberShip.병아리;

    @Column(nullable = false, length = 6)
    private String joinPath;

    @PrePersist
    private void setDefaultValues() {
        if (this.joinPath == null) {
            this.joinPath = "HOME";
        }
    }

    @Column(nullable = true)
    private String snsId;

    @Builder.Default
    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DeliverAddress> deliverAddress = new ArrayList<>();

    @JsonManagedReference
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private WishList wishList;
}
