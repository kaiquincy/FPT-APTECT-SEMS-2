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

        // Các endpoint công khai cho phép POST và GET
        private final String[] PUBLIC_POST_ENDPOINTS = {
            "/api/user",
            "/api/auth/token",
            "/api/auth/introspect",
            "/api/user/forgot-password",
            "/api/user/reset-password",        
        };

        private final String[] PUBLIC_GET_ENDPOINTS = {
            "/api/course/**",
            "/video",
            "/swagger-ui/**",
            "/uploads/**"
        };

        // Các endpoint yêu cầu xác thực người dùng
        private final String[] USER_ENDPOINTS = {
            "/api/user/**",
            "/api/transaction/**",
            "/api/upload-video"
        };

        // Lấy giá trị signerKey từ application.properties
        @Value("${jwt.signerKey}")
        private String signerKey;

        // Tạo bean SecurityFilterChain để cấu hình bảo mật
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                .authorizeHttpRequests(request ->
                    request.requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()
                        .requestMatchers(USER_ENDPOINTS).hasAnyAuthority("SCOPE_STUDENT", "SCOPE_TEACHER", "SCOPE_ADMIN")
                        .requestMatchers("/api/course/**", "/api/lecture/**", "/api/s3bucketstorage/**").hasAnyAuthority("SCOPE_TEACHER", "SCOPE_ADMIN")
                        .requestMatchers("/api/admin/**", "/api/user/**").hasAuthority("SCOPE_ADMIN")
                        .requestMatchers("/api/courses/{course-id}/").hasAuthority("SCOPE_STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/notifications").hasAuthority("SCOPE_ADMIN")
                        .anyRequest().authenticated()) // Tất cả yêu cầu khác phải xác thực

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
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
            );

            // Vô hiệu hóa CSRF
            httpSecurity.csrf(AbstractHttpConfigurer::disable);

            return httpSecurity.build(); // Trả về cấu hình đã được xây dựng
        }

        // Tạo bean JwtDecoder để giải mã JWT
        @Bean
        JwtDecoder jwtDecoder() {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
        }

        // Tạo bean PasswordEncoder để mã hóa mật khẩu
        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
