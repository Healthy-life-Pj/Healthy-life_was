package com.project.healthy_life_was.healthy_life.service;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.FindIdRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.FindInfoRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.response.FindIdResponseDto;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.provider.JwtProvider;
import com.project.healthy_life_was.healthy_life.repository.AuthRepository;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailService {
    private final AuthRepository authRepository;
    private final JwtProvider jwtProvider;

    @Value("${resend.api.key}")
    private String resendApiKey;

    public ResponseDto<String> sendMessageId(FindIdRequestDto dto) {
        try {
            String token = jwtProvider.generateJwtTokenByEmailId(
                  dto.getName(),
                  dto.getUserEmail()

            );
            String link =
                    "https://healthy-life-web-eta.vercel.app/find-id/verify-find-username?token="
                            + token;

            String body = """
                    <h3>HealthyLife 이메일 인증 링크입니다.</h3>
                    <a href="%s">
                        해당 링크를 클릭하여 인증을 완료해 주세요.
                    </a>
                    <p>감사합니다.</p>
                    """.formatted(link);

            Resend resend = new Resend(resendApiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("onboarding@resend.dev")
                    .to(dto.getUserEmail())
                    .subject("HealthyLife 아이디 이메일 인증")
                    .html(body)
                    .build();

            resend.emails().send(params);

            return ResponseDto.setSuccess(
                    ResponseMessage.MESSAGE_TOKEN_SUCCESS,
                    token
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.MESSAGE_SEND_FAIL);
        }
    }

    public ResponseDto<String> sendMessagePw(FindInfoRequestDto dto) {
        try {
            Optional<User> userOptional =
                    authRepository.findByUsernameAndUserEmail(
                            dto.getUsername(),
                            dto.getEmail())
                    ;

            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            User user = userOptional.get();

            String token = jwtProvider.generateJwtToken(
                    user.getUsername(),
                    user.getUserNickName()
            );

            String link =
                    "https://healthy-life-web-eta.vercel.app/find-password/"
                            + token;

            String body = """
                    <h3>%s님 HealthyLife 이메일 인증 링크입니다.</h3>
                    <a href="%s">
                        해당 링크를 클릭하여 인증을 완료해 주세요.
                    </a>
                    <p>감사합니다.</p>
                    """.formatted(user.getUsername(), link);

            Resend resend = new Resend(resendApiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("onboarding@resend.dev")
                    .to(user.getUserEmail())
                    .subject("HealthyLife 비밀번호 재설정")
                    .html(body)
                    .build();

            resend.emails().send(params);

            return ResponseDto.setSuccess(ResponseMessage.MESSAGE_TOKEN_SUCCESS, token);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    public ResponseDto<FindIdResponseDto> verifyEmailId(String token) {
        FindIdResponseDto data = null;
        String name = jwtProvider.getNameFromJwt(token);
        String userEmail = jwtProvider.getUserEmailFromJwt(token);
        try {
            Optional<User> userOptional = authRepository.findByNameAndUserEmail(name, userEmail);
            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA);
            }
            User user = userOptional.get();
            data = new FindIdResponseDto(user);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }
}