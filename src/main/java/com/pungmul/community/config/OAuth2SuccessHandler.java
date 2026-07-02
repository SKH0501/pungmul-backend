package com.pungmul.community.config;

import com.pungmul.community.domain.User;
import com.pungmul.community.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${app.frontend-url:http://localhost:5173}")  // ✅ 추가
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .name(name)
                                .profileImage(picture)
                                .role(User.Role.USER)
                                .build()
                ));

        String token = jwtUtil.generateToken(email);

        // ✅ 신규 유저면 온보딩으로, 기존 유저면 메인으로
        if (!user.isProfileComplete()) {
            response.sendRedirect(frontendUrl + "/onboarding?token=" + token);
        } else {
            response.sendRedirect(frontendUrl + "/oauth/callback?token=" + token);
        }

    }
}