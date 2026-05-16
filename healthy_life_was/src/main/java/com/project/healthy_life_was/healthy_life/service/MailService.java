package com.project.healthy_life_was.healthy_life.service;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.FindIdRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.request.FindInfoRequestDto;
import com.project.healthy_life_was.healthy_life.dto.auth.response.FindIdResponseDto;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.provider.JwtProvider;
import com.project.healthy_life_was.healthy_life.repository.AuthRepository;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailService {
    private final AuthRepository authRepository;
    private final JwtProvider jwtProvider;

    @Value("${mail.sender.email}")
    private String senderEmail;

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public String createMailForId (String token) {
        String body = "";
        body += "<h3> HealthyLife 이메일 인증 링크입니다.</h3>";

        body += "<a href=\"https://healthy-life-web-eta.vercel.app/find-id/verify-find-username?token="
                + token +
                "\"> 해당 링크를 클릭하여 인증을 완료해 주세요.</a>";

        body += "<p>감사합니다.</p>";

        return body;
    }

    public String createMailForPw(
            String username,
            String token
    ) {

        String body = "";

        body += "<h3>"
                + username
                + "님 HealthyLife 이메일 인증 링크입니다.</h3>";

        body += "<a href=\"https://healthy-life-web-eta.vercel.app/find-password/"
                + token +
                "\"> 해당 링크를 클릭하여 인증을 완료해 주세요.</a>";

        body += "<p>감사합니다.</p>";

        return body;
    }

    public void sendMail(
            String toEmail,
            String subject,
            String html
    ) {

        try {

            Email from = new Email(senderEmail);

            Email to = new Email(toEmail);

            Content content = new Content(
                    "text/html",
                    html
            );

            Mail mail = new Mail(
                    from,
                    subject,
                    to,
                    content
            );

            SendGrid sg = new SendGrid(sendGridApiKey);

            Request request = new Request();

            request.setMethod(Method.POST);

            request.setEndpoint("mail/send");

            request.setBody(mail.build());

            sg.api(request);

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }

    public ResponseDto<String> sendMessageId(FindIdRequestDto dto) {
        try {
            Optional<User> userOptional =
                    authRepository.findByNameAndUserEmail(dto.getName(), dto.getUserEmail());

            if(userOptional.isEmpty()){
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }
            String token = jwtProvider.generateJwtTokenByEmailId(dto.getName(), dto.getUserEmail());

            String body =
                    createMailForId(token);

            sendMail(
                    dto.getUserEmail(),
                    "HealthyLife 아이디 이메일 인증",
                    body
            );

            return ResponseDto.setSuccess(
                    ResponseMessage.MESSAGE_TOKEN_SUCCESS,
                    token
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    public ResponseDto<String> sendMessagePw(FindInfoRequestDto dto) {
        try {
            Optional<User> userOptional = authRepository.findByUsernameAndUserEmail(dto.getUsername(), dto.getEmail());

            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            User user = userOptional.get();
            String token = jwtProvider.generateJwtToken(user.getUsername(), user.getUserNickName());

            String body =
                    createMailForPw(
                            user.getUsername(),
                            token
                    );

            sendMail(
                    user.getUserEmail(),
                    "HealthyLife 이메일 인증 링크",
                    body
            );

            return ResponseDto.setSuccess(
                    ResponseMessage.MESSAGE_TOKEN_SUCCESS,
                    token
            );
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