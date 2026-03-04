package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.object.CustomOAuth2User;
import com.project.healthy_life_was.healthy_life.entity.user.Gender;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImplement
        extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(request);

        String registrationId =
                request.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes =
                oAuth2User.getAttributes();

        String snsId;
        String username;
        String nickname = null;
        String name = null;
        String email = null;
        String phone = null;
        String genderStr = null;
        String birthyear = null;
        String birthday = null;

        // 🔵 KAKAO
        if ("kakao".equals(registrationId)) {

            Object idObj = attributes.get("id");
            if (idObj instanceof Long id) {
                snsId = String.valueOf(id);
                username = "kakao_" + snsId;
            } else {
                snsId = null;
                username = null;
            }

            Object kakaoAccountObj = attributes.get("kakao_account");
            if (kakaoAccountObj instanceof Map<?, ?> kakaoAccount) {

                Object profileObj = kakaoAccount.get("profile");
                if (profileObj instanceof Map<?, ?> profile) {
                    nickname = (String) profile.get("profile_nickname");
                }

                name = (String) kakaoAccount.get("name");
                email = (String) kakaoAccount.get("email");
                phone = (String) kakaoAccount.get("phone_number");
                genderStr = (String) kakaoAccount.get("gender");
                birthyear = (String) kakaoAccount.get("birthyear");
                birthday = (String) kakaoAccount.get("birthday");
            }
        }

        // 🟢 NAVER
        else if ("naver".equals(registrationId)) {

            Object responseObj = attributes.get("response");
            if (responseObj instanceof Map<?, ?> response) {

                snsId = (String) response.get("id");
                username = "naver_" + snsId;

                nickname = (String) response.get("nickname");
                name = (String) response.get("name");
                email = (String) response.get("email");
                phone = (String) response.get("mobile");
                genderStr = (String) response.get("gender");
                birthyear = (String) response.get("birthyear");
                birthday = (String) response.get("birthday");
            } else {
                snsId = null;
                username = null;
            }
        } else {
            snsId = null;
            username = null;
        }

        if (username == null) {
            throw new OAuth2AuthenticationException("SNS username 생성 실패");
        }

        if (nickname == null || nickname.isBlank()) {
            nickname = name;
        }

        if (name == null) {
            name = nickname;
        }

        if (email == null) {
            email = username + "@sns.local";
        }

        if (phone == null) {
            phone = "000-0000-0000";
        }

        Date birthDate = new Date();

        try {
            if (birthyear != null && birthday != null) {

                String month;
                String day;

                if (birthday.contains("-")) {
                    // NAVER 형식 MM-DD
                    String[] parts = birthday.split("-");
                    month = parts[0];
                    day = parts[1];
                } else {
                    // KAKAO 형식 MMDD
                    month = birthday.substring(0, 2);
                    day = birthday.substring(2, 4);
                }

                String fullDate = birthyear + "-" + month + "-" + day;

                birthDate = new SimpleDateFormat("yyyy-MM-dd")
                        .parse(fullDate);
            }
        } catch (Exception ignored) {}

        Gender gender;
        if ("female".equalsIgnoreCase(genderStr)
                || "F".equalsIgnoreCase(genderStr)) {
            gender = Gender.F;
        } else {
            gender = Gender.M;
        }

        String finalName = name;
        String finalNickname = nickname;
        String finalEmail = email;
        String finalPhone = phone;
        Date finalBirthDate = birthDate;
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> {

                    User newUser = User.builder()
                            .username(username)
                            .password("SNS_LOGIN")
                            .name(finalName)
                            .userNickName(finalNickname)
                            .userEmail(finalEmail)
                            .userPhone(finalPhone)
                            .userBirth(finalBirthDate)
                            .userGender(gender)
                            .joinPath(registrationId.toUpperCase())
                            .snsId(snsId)
                            .build();

                    return userRepository.save(newUser);
                });

    System.out.println("NAVER RESPONSE: " + attributes);
        return new CustomOAuth2User(user, attributes);
    }
}
