package com.project.healthy_life_was.healthy_life.entity.whishList;

import com.project.healthy_life_was.healthy_life.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wish_lists")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_list_id")
    private Long wishListId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "wishList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishListItem> wishListItems = new ArrayList<>();
}
