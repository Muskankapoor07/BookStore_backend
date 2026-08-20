package com.bookstore.bookstore.security;

import com.bookstore.bookstore.enums.Role;
import com.bookstore.bookstore.model.User;
import com.bookstore.bookstore.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository) {

        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Authorization header nahi hai
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer ke baad actual JWT token
        String token = authHeader.substring(7);

        try {

            // JWT se email extract karo
            String email = jwtService.extractEmail(token);

            if (email != null
                    && SecurityContextHolder.getContext()
                    .getAuthentication() == null
                    && jwtService.isTokenValid(token)) {

                // Email se user database mein find karo
                User user = userRepository.findByEmail(email)
                        .orElse(null);

                if (user != null) {

                    /*
                     * ADMIN ko email verification ki zarurat nahi.
                     *
                     * CUSTOMER ko verified hona required hai.
                     */
                    boolean allowed = user.getRole() == Role.ADMIN
                            || user.isVerified();

                    if (allowed) {

                        SimpleGrantedAuthority authority =
                                new SimpleGrantedAuthority(
                                        "ROLE_" + user.getRole().name()
                                );

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        email,
                                        null,
                                        List.of(authority)
                                );

                        authentication.setDetails(
                                new WebAuthenticationDetailsSource()
                                        .buildDetails(request)
                        );

                        SecurityContextHolder
                                .getContext()
                                .setAuthentication(authentication);
                    }
                }
            }

        } catch (Exception e) {
            // Invalid/expired token hone par
            // request unauthenticated rahegi
        }

        filterChain.doFilter(request, response);
    }
}