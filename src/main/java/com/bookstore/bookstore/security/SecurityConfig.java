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

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            UserRepository userRepository) {

        return username -> userRepository.findByEmail(username)
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

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // ================= PUBLIC APIs =================
                        .requestMatchers(
                                "/bookstore_user/register",
                                "/bookstore_user/login",
                                "/bookstore_user/verification/**",

                                // Forgot & Reset Password
                                "/bookstore_user/forgot-password",
                                "/bookstore_user/reset-password",

                                // Admin authentication
                                "/bookstore_user/admin/registration",
                                "/bookstore_user/admin/login",

                                // Swagger
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // ================= ADMIN APIs =================
                        .requestMatchers(
                                "/bookstore_user/admin/**"
                        ).hasRole("ADMIN")

                        // ================= OTHER APIs =================
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form.disable())

                .httpBasic(basic -> basic.disable())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}