package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.deliverAddress.DeliverAddressDto;
import com.project.healthy_life_was.healthy_life.dto.user.request.PasswordUpdateRequestDto;
import com.project.healthy_life_was.healthy_life.dto.user.request.UserDeleteRequestDto;
import com.project.healthy_life_was.healthy_life.dto.user.request.UserUpdateRequestDto;
import com.project.healthy_life_was.healthy_life.dto.user.response.UserInfoResponseDto;
import com.project.healthy_life_was.healthy_life.entity.deliverAddress.DeliverAddress;
import com.project.healthy_life_was.healthy_life.entity.user.Gender;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.repository.DeliverAddressRepository;
import com.project.healthy_life_was.healthy_life.provider.JwtProvider;
import com.project.healthy_life_was.healthy_life.repository.UserRepository;
import com.project.healthy_life_was.healthy_life.service.UserService;
import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {
    private final UserRepository userRepository;
    private final DeliverAddressRepository deliverAddressRepository;
    private final BCryptPasswordEncoder bCryptpasswordEncoder;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean checkPassword(String currentPassword, String encodedPassword) {
        return passwordEncoder.matches(currentPassword, encodedPassword);
    }

    @Override
    public ResponseDto<UserInfoResponseDto> getUserInfo(String username) {
        UserInfoResponseDto data = null;
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_USER));

        data = new UserInfoResponseDto(user);

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UserInfoResponseDto> updateUserInfo(String username, UserUpdateRequestDto dto) {
        UserInfoResponseDto data = null;
        String inputName = dto.getName();
        String inputNickName = dto.getUserNickName();
        String inputEmail = dto.getUserEmail();
        String inputPhone = dto.getUserPhone();
        Date inputBirth = dto.getUserBirth();
        Gender inputGender = dto.getUserGender();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_USER));

        User updatedUser = user.toBuilder()
                .name(inputName != null? inputName : user.getName())
                .userNickName(inputNickName != null? inputNickName : user.getUserNickName())
                .userEmail(inputEmail != null? inputEmail : user.getUserEmail())
                .userPhone(inputPhone != null? inputPhone : user.getUserPhone())
                .userBirth(inputBirth != null? inputBirth : user.getUserBirth())
                .userGender(inputGender != null? inputGender : user.getUserGender())
                .build();

        userRepository.save(updatedUser);

        data = new UserInfoResponseDto(updatedUser);

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<Void> updatePwByMyPage(String username, PasswordUpdateRequestDto dto) {
        String currentPassword = dto.getCurrentPassword();
        String password = dto.getUserPassword();
        String confirmUserPassword = dto.getConfirmUserPassword();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_USER));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ResponseDto.setFailed(ResponseMessage.INCORRECT_CURRENT_PASSWORD);
        }

        if (!password.equals(confirmUserPassword)) {
            return ResponseDto.setFailed(ResponseMessage.PASSWORD_MISMATCH);
        }

        String encodePassword = bCryptpasswordEncoder.encode(password);
        User updatedUser = user.toBuilder()
                .password(encodePassword)
                .build();
        userRepository.save(updatedUser);

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }

    @Override
    public ResponseDto<Void> updatePwByEmailToken(String token, PasswordUpdateRequestDto dto) {
        String password = dto.getUserPassword();
        String confirmUserPassword = dto.getConfirmUserPassword();
        String username = jwtProvider.getUsernameFromJwt(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_USER));

        if (!password.equals(confirmUserPassword)) {
            return ResponseDto.setFailed(ResponseMessage.PASSWORD_MISMATCH);
        }

        String encodePassword = bCryptpasswordEncoder.encode(password);
        User updatedUser = user.toBuilder()
                .password(encodePassword)
                .build();
        userRepository.save(updatedUser);

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }

    @Override
    public ResponseDto<Void> deleteUser(String username, UserDeleteRequestDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_USER));

        if (!passwordEncoder.matches(dto.getUserPassword(), user.getPassword())) {
            return ResponseDto.setFailed(ResponseMessage.NOT_MATCH_PASSWORD);
        }
        userRepository.delete(user);

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }
}