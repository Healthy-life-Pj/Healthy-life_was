package com.project.healthy_life_was.healthy_life.handler;

import com.project.healthy_life_was.healthy_life.common.object.CustomOAuth2User;
import com.project.healthy_life_was.healthy_life.provider.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        CustomOAuth2User oAuth2User =
                (CustomOAuth2User) authentication.getPrincipal();

        String username = oAuth2User.getName();

        String token = jwtProvider.createOAuthToken(username);

        int exprTime = jwtProvider.getExpiration();

        response.sendRedirect(
                "http://localhost:3000/oauth?token="
                        + token
                        + "&exprTime="
                        + exprTime
        );
    }
}
