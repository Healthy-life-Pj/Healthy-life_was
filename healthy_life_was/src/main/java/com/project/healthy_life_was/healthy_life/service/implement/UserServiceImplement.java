package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.user.request.PasswordUpdateRequestDto;
import com.project.healthy_life_was.healthy_life.dto.user.request.UserDeleteRequestDto;
import com.project.healthy_life_was.healthy_life.dto.user.request.UserUpdateRequestDto;
import com.project.healthy_life_was.healthy_life.dto.user.response.UserInfoResponseDto;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.repository.DeliverAddressRepository;
import com.project.healthy_life_was.healthy_life.provider.JwtProvider;
import com.project.healthy_life_was.healthy_life.repository.UserRepository;
import com.project.healthy_life_was.healthy_life.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean checkPassword(String currentPassword, String encodedPassword) {
        return passwordEncoder.matches(currentPassword, encodedPassword);
    }

    @Override
    public ResponseDto<UserInfoResponseDto> getUserInfo(String username) {
        UserInfoResponseDto data = null;
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_USER));

        data = new UserInfoResponseDto(user);

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<UserInfoResponseDto> updateUserInfo(String username, UserUpdateRequestDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_USER));

        if (dto.getName() != null)         user.setName(dto.getName());
        if (dto.getUserNickName() != null)  user.setUserNickName(dto.getUserNickName());
        if (dto.getUserEmail() != null)     user.setUserEmail(dto.getUserEmail());
        if (dto.getUserPhone() != null)     user.setUserPhone(dto.getUserPhone());
        if (dto.getUserBirth() != null)     user.setUserBirth(dto.getUserBirth());
        if (dto.getUserGender() != null)    user.setUserGender(dto.getUserGender());

        userRepository.save(user);

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, new UserInfoResponseDto(user));
    }

    @Override
    @Transactional
    public ResponseDto<Void> updatePwByMyPage(String username, PasswordUpdateRequestDto dto) {
        String currentPassword = dto.getCurrentPassword();
        String password = dto.getUserPassword();
        String confirmUserPassword = dto.getConfirmUserPassword();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_USER));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ResponseDto.setFailed(ResponseMessage.INCORRECT_CURRENT_PASSWORD);
        }

        if (!password.equals(confirmUserPassword)) {
            return ResponseDto.setFailed(ResponseMessage.PASSWORD_MISMATCH);
        }

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }

    @Override
    public ResponseDto<Void> deleteUser(String username, UserDeleteRequestDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_USER));

        if (!passwordEncoder.matches(dto.getUserPassword(), user.getPassword())) {
            return ResponseDto.setFailed(ResponseMessage.NOT_MATCH_PASSWORD);
        }
        userRepository.delete(user);

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }
}