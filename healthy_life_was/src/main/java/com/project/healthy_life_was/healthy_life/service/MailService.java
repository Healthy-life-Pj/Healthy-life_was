package com.project.healthy_life_was.healthy_life.service;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.FindIdRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.response.FindIdResponseDto;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.provider.JwtProvider;
import com.project.healthy_life_was.healthy_life.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final AuthRepository authRepository;
    private final JwtProvider jwtProvider;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public MimeMessage createMailForId (String email, String token) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, email);

        String subject;
        String body;

        subject = "HealthyLife 이메일 인증 링크";
        body = "<h3> HealthyLife 이메일 인증 링크입니다.</h3>";
        body += "<a href=\"http://localhost:3000/find-id?token=" + token + "\"> 해당 링크를 클릭하여 인증을 완료해 주세요.</a>";
        body += "<p>감사합니다.</p>";

        message.setSubject(subject);
        message.setText(body, "UTF-8", "html");
        return message;
    }

    public MimeMessage createMailForPw(String email, String username, String token) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, email);

        String subject;
        String body;

        subject = "HealthyLife 이메일 인증 링크";
        body = "<h3>" + username + "님 HealthyLife 이메일 인증 링크입니다.</h3>";
        body += "<a href=\"http://localhost:3000/find-password/" + token + "\"> 해당 링크를 클릭하여 인증을 완료해 주세요.</a>";
        body += "<p>감사합니다.</p>";

        message.setSubject(subject);
        message.setText(body, "UTF-8", "html");
        return message;
    }

    public void sendMail(MimeMessage message) throws MessagingException {
        javaMailSender.send(message);
    }

    public ResponseDto<String> sendMessageId(FindIdRequestDto dto) throws MessagingException {
        try {
            String token = jwtProvider.generateJwtTokenByEmailId(dto.getName(), dto.getUserEmail());

            MimeMessage message = createMailForId(dto.getUserEmail(), token);
            try {
                javaMailSender.send(message);
                return ResponseDto.setSuccess(ResponseMessage.MESSAGE_TOKEN_SUCCESS, token);
            } catch (MailException e) {
                e.printStackTrace();
                return ResponseDto.setFailed(ResponseMessage.MESSAGE_SEND_FAIL);
            }

        } catch (MailException e) {
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