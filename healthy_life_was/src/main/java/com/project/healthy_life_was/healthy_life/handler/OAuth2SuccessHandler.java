package com.project.healthy_life_was.healthy_life.handler;

import com.project.healthy_life_was.healthy_life.common.object.CustomOAuth2User;
import com.project.healthy_life_was.healthy_life.provider.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        // 1. OAuth2User에서 정보 추출
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String userEmail = oAuth2User.getName();
        boolean existed = oAuth2User.isExisted();

        // 2. JWT 토큰 생성
        String token = jwtProvider.generateJwtTokenByEmail(userEmail);

        // 3. 프론트엔드로 리다이렉트
        String redirectUrl;
        if (existed) {
            // 기존 회원 → 메인 페이지
            redirectUrl = String.format(
                    "http://localhost:3000/oauth-redirect?token=%s&expirationTime=%d",
                    token,
                    3600000  // 1시간
            );
        } else {
            // 신규 회원 → 추가 정보 입력 페이지
            redirectUrl = String.format(
                    "http://localhost:3000/oauth-signup?token=%s&expirationTime=%d",
                    token,
                    3600000
            );
        }

        response.sendRedirect(redirectUrl);
    }
}
