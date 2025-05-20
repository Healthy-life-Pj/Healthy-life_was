package com.project.healthy_life_was.healthy_life.entity.deliverAddress;

import com.project.healthy_life_was.healthy_life.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deliver_address")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeliverAddress {
    @Id
    @Column (name = "address_deliver_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliverAddressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "user_id", nullable = false)
    private User user;

    @Column (name = "address")
    private String address;

    @Column (name = "address_detail")
    private String addressDetail;

    @Column (name = "post_num")
    private int postNum;
}