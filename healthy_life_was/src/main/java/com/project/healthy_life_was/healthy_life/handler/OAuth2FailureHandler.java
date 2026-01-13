package com.project.healthy_life_was.healthy_life.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {

        // 로그인 실패 시 에러 메시지와 함께 리다이렉트
        String redirectUrl = String.format(
                "http://localhost:3000/login?error=%s",
                exception.getMessage()
        );

        response.sendRedirect(redirectUrl);
    }
}
