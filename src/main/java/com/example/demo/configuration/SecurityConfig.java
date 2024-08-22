package com.example.demo.configuration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.dto.ApiResponse;
import com.example.demo.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] PUBLIC_POST_ENDPOINTS = {
        "/api/user",
        "/api/auth/token", "/api/auth/introspect"
    };
    private final String[] PUBLIC_GET_ENDPOINTS = {
        "/api/course/**",
        "/video"
    };
    private final String[] USER_ENDPOINTS = {

        "/api/user/**",
        "/api/enrollment/**",
        "/api/transaction/**",
        "/api/upload-video"
    };

    // @Value lấy giá trị từ application.properties cho signerKey, dùng để ký JWT
    @Value("${jwt.signerKey}")
    private String signerKey;

    // @Bean tạo ra một bean SecurityFilterChain, đây là nơi cấu hình bảo mật chính của ứng dụng
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Cấu hình bảo mật các yêu cầu HTTP
        httpSecurity
            .authorizeHttpRequests(request ->
                // Cho phép POST yêu cầu tới các PUBLIC_ENDPOINTS mà không cần xác thực
                request.requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()
                    .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()
                    .requestMatchers(USER_ENDPOINTS).hasAnyAuthority("SCOPE_USER", "SCOPE_TEACHER", "SCOPE_ADMIN")
                    .requestMatchers("/api/course/**", "/api/lecture/**","/api/s3bucketstorage/**").hasAnyAuthority("SCOPE_TEACHER", "SCOPE_ADMIN")
                    .requestMatchers("/api/admin/**", "/api/user/**").hasAuthority("SCOPE_ADMIN")
                    // Mọi yêu cầu khác phải được xác thực
                    .anyRequest().authenticated())
                    
            .exceptionHandling(exceptionHandling ->
                exceptionHandling
                    .authenticationEntryPoint((request, response, authException) -> {
                        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
                        ApiResponse<String> apiResponse = new ApiResponse<>();
                        apiResponse.setCode(errorCode.getCode());
                        apiResponse.setMessage(errorCode.getMessage());
                        
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        String jsonResponse = new ObjectMapper().writeValueAsString(apiResponse);
                        response.getWriter().write(jsonResponse);
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
                        ApiResponse<String> apiResponse = new ApiResponse<>();
                        apiResponse.setCode(errorCode.getCode());
                        apiResponse.setMessage(errorCode.getMessage());

                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        String jsonResponse = new ObjectMapper().writeValueAsString(apiResponse);
                        response.getWriter().write(jsonResponse);
                    })
    );

        // Cấu hình xác thực OAuth2 cho các yêu cầu dựa trên JWT
        httpSecurity.oauth2ResourceServer(oauth2 ->
            // Sử dụng jwtDecoder() để giải mã JWT và xác thực
            oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
        );

        // Vô hiệu hóa CSRF (Cross-Site Request Forgery) protection, thường cần thiết khi sử dụng APIs RESTful
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        
        // Trả về cấu hình SecurityFilterChain đã được xây dựng
        return httpSecurity.build();
    }

     // @Bean tạo ra một bean JwtDecoder dùng để giải mã JWT
    @Bean
    JwtDecoder jwtDecoder(){
        // Tạo SecretKeySpec từ signerKey với thuật toán HS512
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
         // Trả về một NimbusJwtDecoder được cấu hình để sử dụng khóa bí mật và thuật toán HS512
        return NimbusJwtDecoder
            .withSecretKey(secretKeySpec)
            .macAlgorithm(MacAlgorithm.HS512)
            .build();
            
    };

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}