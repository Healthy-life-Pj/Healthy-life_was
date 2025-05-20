package com.project.healthy_life_was.healthy_life.dto.auth.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.healthy_life_was.healthy_life.dto.deliverAddress.DeliverAddressDto;
import com.project.healthy_life_was.healthy_life.entity.deliverAddress.DeliverAddress;
import com.project.healthy_life_was.healthy_life.entity.user.Gender;
import com.project.healthy_life_was.healthy_life.entity.user.MemberShip;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpResponseDto {

    private Long userId;

    private String username;

    private String password;

    private String userNickName;

    private Date userBirth;

    private Gender userGender;

    private String userEmail;

    private String userPhone;

    private String joinPath;

    private String snsId;

    private List<DeliverAddressDto> deliverAddress = new ArrayList<>();

    private MemberShip userMemberGrade;

    public SignUpResponseDto(User user, List<DeliverAddress> deliverAddressList) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.userNickName = user.getUserNickName();
        this.userBirth = user.getUserBirth();
        this.userGender = user.getUserGender();
        this.userEmail = user.getUserEmail();
        this.userPhone = user.getUserPhone();
        this.userMemberGrade = user.getUserMemberGrade();
        this.joinPath = user.getJoinPath();
        this.snsId = user.getSnsId();

        this.deliverAddress = deliverAddressList.stream()
                .map(address -> new DeliverAddressDto(address.getAddress(), address.getAddressDetail(), address.getPostNum()))
                .collect(Collectors.toList());
    }

}
