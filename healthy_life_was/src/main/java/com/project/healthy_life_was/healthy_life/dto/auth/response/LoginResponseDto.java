package com.project.healthy_life_was.healthy_life.dto.auth.response;

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
public class LoginResponseDto {

    private Long userId;

    private String username;

    private String userNickName;

    private Date userBirth;

    private Gender userGender;

    private String userEmail;

    private String userPhone;

    private List<DeliverAddressDto> deliverAddress = new ArrayList<>();

    private MemberShip userMemberGrade;

    private String token;

    private int exprTime;

    public LoginResponseDto(User user, List<DeliverAddressDto> deliverAddressList, String token, int exprTime) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.userNickName = user.getUserNickName();
        this.userBirth = user.getUserBirth();
        this.userGender = user.getUserGender();
        this.userEmail = user.getUserEmail();
        this.userPhone = user.getUserPhone();
        this.deliverAddress = deliverAddressList;
        this.userMemberGrade = user.getUserMemberGrade();
        this.token = token;
        this.exprTime = exprTime;

        this.deliverAddress = deliverAddressList.stream()
                .map(address -> new DeliverAddressDto(address.getAddress(), address.getAddressDetail(), address.getPostNum()))
                .collect(Collectors.toList());
    }

}
