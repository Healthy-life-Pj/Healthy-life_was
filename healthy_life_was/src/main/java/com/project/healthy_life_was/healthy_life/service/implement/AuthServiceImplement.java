package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.FindInfoRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.LoginRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.SignUpRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.response.FindInfoResponseDto;
import com.project.healthy_life_was.healthy_life.dto.auth.response.LoginResponseDto;
import com.project.healthy_life_was.healthy_life.dto.auth.response.SignUpResponseDto;
import com.project.healthy_life_was.healthy_life.dto.deliverAddress.DeliverAddressDto;
import com.project.healthy_life_was.healthy_life.entity.deliverAddress.DeliverAddress;
import com.project.healthy_life_was.healthy_life.entity.user.Gender;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.entity.whishList.WishList;
import com.project.healthy_life_was.healthy_life.provider.JwtProvider;
import com.project.healthy_life_was.healthy_life.repository.AuthRepository;
import com.project.healthy_life_was.healthy_life.repository.DeliverAddressRepository;
import com.project.healthy_life_was.healthy_life.repository.UserRepository;
import com.project.healthy_life_was.healthy_life.service.AuthService;
import com.project.healthy_life_was.healthy_life.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImplement implements AuthService {
    private final AuthRepository authRepository;
    private final DeliverAddressRepository deliverAddressRepository;
    private final BCryptPasswordEncoder bCryptpasswordEncoder;
    private final JwtProvider jwtProvider;
    private final MailService mailService;
    private final JavaMailSender javaMailSender;

    @Override
    public ResponseDto<SignUpResponseDto> signUp(SignUpRequestDto dto) {
       SignUpResponseDto data = null;

       String username = dto.getUsername();
       String password = dto.getPassword();
       String confirmPassword = dto.getConfirmPassword();
       String name = dto.getName();
       String userNickName = dto.getUserNickName();
       Date userBirth = dto.getUserBirth();
       String userEmail = dto.getUserEmail();
       String userPhone = dto.getUserPhone();
       Gender userGender = dto.getUserGender();
       String address = dto.getAddress();
       String joinPath = dto.getJoinPath();
       String snsId = dto.getSnsId();
       String addressDetail = dto.getAddressDetail();
       int postNum =dto.getPostNum();

        if (username == null || username.trim().isEmpty() || !username.matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,15}$")) {
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL + "username");
        }

        if (password == null || password.trim().isEmpty() || !password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,16}$")) {
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL + "password");
        }

        if (confirmPassword == null || confirmPassword.trim().isEmpty() || !confirmPassword.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,16}$")) {
           return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL + "confirmPassword");
       }

       if (!password.equals(confirmPassword)) {
           return ResponseDto.setFailed(ResponseMessage.NOT_MATCH_PASSWORD + "confirmPassword");
       }

       if (name == null || name.isEmpty() || !name.matches("^[가-힣a-zA-Z]{2,10}$")) {
           return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL + "name");
       }

       if (userNickName == null || userNickName.trim().isEmpty() || !userNickName.matches("^[가-힣a-zA-Z\\d]{3,10}$")) {
           return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL + "userNickName");
       }

       if (userPhone == null || userPhone.isEmpty() || !userPhone.matches("^01[016789]\\d{7,8}$")) {
           return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL + "userPhone");
       }

       if (userEmail == null || userEmail.isEmpty() || !userEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
           return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL + "userEmail");
       }

       if (userGender == null) {
           return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL + "userGender");
       }

       if (userBirth == null) {
           return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL + "userBirth");
       }

       if (authRepository.existsByUsername(username)) {
           return ResponseDto.setFailed(ResponseMessage.EXIST_USER_NAME);
       }

//       if (authRespository.existsByUserEmail(userEmail)) {
//           return ResponseDto.setFailed(ResponseMessage.EXIST_USER_EMAIL);
//       }

       if (authRepository.existsByUserNickName(userNickName)) {
           return ResponseDto.setFailed(ResponseMessage.EXIST_USER_NICK_NAME);
       }

       try {
           String encodePassword = bCryptpasswordEncoder.encode(password);
           WishList wishList = new WishList();
           User user = User.builder()
                    .username(username)
                    .password(encodePassword)
                    .name(name)
                    .userNickName(userNickName)
                    .userBirth(userBirth)
                    .userPhone(userPhone)
                    .userEmail(userEmail)
                    .userGender(userGender)
                    .wishList(new WishList())
                    .joinPath(joinPath)
                    .snsId(snsId)
                    .build();
           wishList.setUser(user);
           user.setWishList(wishList);
           User savedUser = authRepository.save(user);
           DeliverAddress deliveraddress = DeliverAddress.builder()
                   .user(savedUser)
                   .address(address)
                   .addressDetail(addressDetail)
                   .postNum(postNum)
                   .build();
           deliverAddressRepository.save(deliveraddress);
           savedUser.getDeliverAddress().add(deliveraddress);
           data = new SignUpResponseDto(user, List.of(deliveraddress));
       } catch (Exception e) {
           e.printStackTrace();
           return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
       }
       return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<LoginResponseDto> login(LoginRequestDto dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();

        LoginResponseDto data = null;

        if (username == null || username.isEmpty()) {
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL + "username");
        }

        if (password == null || password.isEmpty()) {
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL + "password");
        }

        try {
            User user = authRepository.findByUsername(username);
            List<DeliverAddress> deliverAddressList = deliverAddressRepository.findByUser_UserId(user.getUserId());

            if (user == null) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }
            if (!bCryptpasswordEncoder.matches(password, user.getPassword())) {
                return ResponseDto.setFailed(ResponseMessage.NOT_MATCH_PASSWORD);
            }

            String token = jwtProvider.generateJwtToken(username);
            int exprTime = jwtProvider.getExpiration();

            List<DeliverAddressDto> deliverAddressDtoList = deliverAddressList.stream()
                    .map(address -> new DeliverAddressDto(address.getAddress(), address.getAddressDetail(), address.getPostNum()))
                    .collect(Collectors.toList());

            data = new LoginResponseDto(user, deliverAddressDtoList, token, exprTime);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.SIGN_IN_FAIL);
        }

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<Boolean> duplicateUserName(String username) {
        try {
            boolean result = authRepository.existsByUsername(username);

            if (result == true) {
                return ResponseDto.setSuccess(ResponseMessage.DUPLICATED_USER_NAME, result);
            } else {
                return ResponseDto.setSuccess(ResponseMessage.SUCCESS, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public ResponseDto<Boolean> duplicateUserNickName(String userNickName) {
        try {
            boolean result = authRepository.existsByUserNickName(userNickName);

            if (result == true) {
                return ResponseDto.setSuccess(ResponseMessage.DUPLICATED_USER_NAME, result);
            } else {
                return ResponseDto.setSuccess(ResponseMessage.SUCCESS, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public ResponseDto<FindInfoResponseDto> recoveryEmail(FindInfoRequestDto dto) throws MessagingException {
        String email = dto.getEmail();
        String username = dto.getUsername();

        try {
            if(username == null) {
                Optional<User> user = authRepository.findByUserEmail(email);

                if (user.isEmpty()) {
                    return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
                }

                String token = jwtProvider.generateJwtTokenByEmail(email);
                MimeMessage message = mailService.createMailForId(email, token);
                javaMailSender.send(message);
                return ResponseDto.setSuccess(ResponseMessage.SUCCESS, new FindInfoResponseDto(message, user.get().getUsername(), null));
            } else {
                String token = jwtProvider.generateJwtToken(username);
                MimeMessage message = mailService.createMailForPw(email, username, token);
                javaMailSender.send(message);
                return ResponseDto.setSuccess(ResponseMessage.SUCCESS, new FindInfoResponseDto(message, null, token));
            }
        } catch (Exception e) {
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public ResponseDto<LoginResponseDto> oauthLogin(String email, String name, String snsId, String provider) {
        try {
            Optional<User> existingUser = authRepository.findBySnsIdAndJoinPath(snsId, provider);

            User user;
            if (existingUser.isPresent()) {
                user = existingUser.get();
            } else {
                user = createOAuthUser(email, name, snsId, provider);
            }

            String token = jwtProvider.generateJwtToken(user.getUsername());
            int exprTime = jwtProvider.getExpiration();

            List<DeliverAddress> deliverAddressList = deliverAddressRepository.findByUser_UserId(user.getUserId());
            List<DeliverAddressDto> deliverAddressDtoList = deliverAddressList.stream()
                    .map(address -> new DeliverAddressDto(address.getAddress(), address.getAddressDetail(), address.getPostNum()))
                    .collect(Collectors.toList());

            LoginResponseDto data = new LoginResponseDto(user, deliverAddressDtoList, token, exprTime);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.SIGN_IN_FAIL);
        }
    }

    private User createOAuthUser(String email, String name, String snsId, String provider) {
        String username = provider.toLowerCase() + "_" + email.split("@")[0] + "_" + System.currentTimeMillis();
        String defaultPassword = bCryptpasswordEncoder.encode("OAUTH_" + snsId);

        WishList wishList = new WishList();
        User user = User.builder()
                .username(username)
                .password(defaultPassword)
                .name(name)
                .userNickName(name + "_" + (int)(Math.random() * 10000))
                .userEmail(email)
                .userGender(Gender.M)
                .userBirth(new Date())
                .userPhone("010-0000-0000")
                .joinPath(provider)
                .snsId(snsId)
                .wishList(new WishList())
                .build();

        wishList.setUser(user);
        user.setWishList(wishList);
        return authRepository.save(user);
    }
}