package com.pungmul.community.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtFilter jwtFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("https://pungmul-frontend.vercel.app");  // 추가!
        config.addAllowedOrigin("https://pungmul.store");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // 추가!
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/oauth2/**",
                                "/login/oauth2/**",
                                "/api/**",
                                "/upload",
                                "/upload/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()  // ✅ 조회는 누구나
                        .requestMatchers("/api/users/me").authenticated()        // ✅ 내 정보는 로그인 필요
                        .requestMatchers("/api/me").authenticated()              // ✅ 기존 me API
                        .requestMatchers(HttpMethod.POST, "/api/**").authenticated()   // ✅ 등록은 로그인
                        .requestMatchers(HttpMethod.PATCH, "/api/**").authenticated()  // ✅ 수정은 로그인
                        .requestMatchers(HttpMethod.DELETE, "/api/**").authenticated() // ✅ 삭제는 로그인
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2SuccessHandler)
                )
                .addFilterBefore(jwtFilter,
                        org.springframework.security.web.authentication
                                .UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}