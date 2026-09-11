package com.bookstore.bookstore.security;

import com.bookstore.bookstore.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.googleOAuth2SuccessHandler = googleOAuth2SuccessHandler;
    }

    // ================= PASSWORD ENCODER =================

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ================= USER DETAILS SERVICE =================

    @Bean
    public UserDetailsService userDetailsService(
            UserRepository userRepository) {

        return username ->
                userRepository.findByEmail(username)
                        .map(user ->
                                org.springframework.security.core.userdetails.User
                                        .withUsername(user.getEmail())
                                        .password(user.getPassword())
                                        .roles(user.getRole().name())
                                        .build()
                        )
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "User not found"
                                )
                        );
    }

    // ================= SECURITY FILTER CHAIN =================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                // Enable CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // CSRF disable because REST API
                .csrf(csrf -> csrf.disable())

                // JWT based authentication
                                // Exception handling for REST API
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\":\"Session expired. Please login again.\"}");
                        })
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // ================= AUTHORIZATION =================

                .authorizeHttpRequests(auth -> auth

                        // ---------- PUBLIC USER APIs ----------
                        .requestMatchers(
                                "/bookstore_user/register",
                                "/bookstore_user/login",
                                "/bookstore_user/verification/**",
                                "/bookstore_user/forgot-password",
                                "/bookstore_user/reset-password"
                        ).permitAll()

                        // ---------- PUBLIC PRODUCT APIs ----------
                        .requestMatchers(
                                "/bookstore_user/search/book",
                                "/bookstore_user/get/book",
                                "/bookstore_user/get/book/*"
                        ).permitAll()

                        // ---------- PUBLIC ADMIN AUTH ----------
                        .requestMatchers(
                                "/bookstore_user/admin/registration",
                                "/bookstore_user/admin/login"
                        ).permitAll()

                        // ---------- SWAGGER ----------
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // ---------- GOOGLE OAUTH2 ----------
                        .requestMatchers(
                                "/oauth2/**",
                                "/login/oauth2/**"
                        ).permitAll()

                        // ---------- ADMIN APIs ----------
                        .requestMatchers(
                                "/bookstore_user/admin/**"
                        ).hasRole("ADMIN")

                        // ---------- EVERYTHING ELSE ----------
                        .anyRequest().authenticated()
                )

                // Disable default form login
                .formLogin(form -> form.disable())

                // Disable HTTP Basic
                .httpBasic(basic -> basic.disable())

                // ================= GOOGLE LOGIN =================

                .oauth2Login(oauth2 ->
                        oauth2.successHandler(
                                googleOAuth2SuccessHandler
                        )
                )

                // ================= JWT FILTER =================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // ================= CORS CONFIGURATION =================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:3000", "http://localhost:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}