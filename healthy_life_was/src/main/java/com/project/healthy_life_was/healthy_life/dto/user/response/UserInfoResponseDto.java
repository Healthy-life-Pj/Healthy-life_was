package com.project.healthy_life_was.healthy_life.dto.user.response;

import com.project.healthy_life_was.healthy_life.dto.deliverAddress.DeliverAddressDto;
import com.project.healthy_life_was.healthy_life.entity.user.Gender;
import com.project.healthy_life_was.healthy_life.entity.user.MemberShip;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserInfoResponseDto {

    private String username;

    private String  name;

    private String userNickName;

    private String userEmail;

    private String userPhone;

    private Date userBirth;

    private Gender userGender;

    private MemberShip userMemberGrade;

    private List<DeliverAddressDto> deliverAddressInfo;

    public UserInfoResponseDto(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.userNickName = user.getUserNickName();
        this.userBirth = user.getUserBirth();
        this.userGender = user.getUserGender();
        this.userEmail = user.getUserEmail();
        this.userPhone = user.getUserPhone();
        this.userMemberGrade = user.getUserMemberGrade();
        this.deliverAddressInfo = user.getDeliverAddress()
                .stream()
                .map((address) -> {
                    return new DeliverAddressDto(address.getAddress(), address.getAddressDetail(), address.getPostNum());
                }).collect(Collectors.toList());
    }
}