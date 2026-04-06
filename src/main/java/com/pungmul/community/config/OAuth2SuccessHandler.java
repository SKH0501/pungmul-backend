package com.pungmul.community.config;

import com.pungmul.community.domain.User;
import com.pungmul.community.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
/*

## 이 코드가 하는 일

구글 로그인 성공하면 자동으로 실행돼요.
```
1. 구글에서 받은 email, name, picture 꺼내기
2. DB에 이미 가입한 유저인지 확인
3. 없으면 자동 회원가입
4. JWT 토큰 발급
5. 프론트엔드로 토큰 전달

*/


@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        // DB에 유저가 없으면 자동 회원가입
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .name(name)
                                .profileImage(picture)
                                .role(User.Role.USER)
                                .build()
                ));

        // JWT 토큰 발급
        String token = jwtUtil.generateToken(email);

        // 토큰을 프론트엔드로 전달 (React URL로 변경 완료)
        response.sendRedirect("http://localhost:5173/oauth/callback?token=" + token);
    }
}