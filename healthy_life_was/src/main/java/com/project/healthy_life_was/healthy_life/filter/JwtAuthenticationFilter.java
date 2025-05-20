package com.project.healthy_life_was.healthy_life.filter;

import com.project.healthy_life_was.healthy_life.provider.JwtProvider;
import com.project.healthy_life_was.healthy_life.repository.UserRepository;
import com.project.healthy_life_was.healthy_life.security.PrincipalUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");

            String token = (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
                    ? jwtProvider.removeBearer(authorizationHeader)
                    : null;
            if (token == null || !jwtProvider.isValidToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }
            String username = jwtProvider.getUsernameFromJwt(token);
            setAuthenticationContext(request, username);

        } catch(Exception e) {
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthenticationContext(HttpServletRequest request, String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            PrincipalUser principalUser = new PrincipalUser(user);

            AbstractAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(principalUser, null, principalUser.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authenticationToken);

            SecurityContextHolder.setContext(securityContext);
        });
    }
}