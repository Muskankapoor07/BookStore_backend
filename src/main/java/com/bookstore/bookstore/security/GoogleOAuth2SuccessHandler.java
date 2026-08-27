package com.bookstore.bookstore.security;

import com.bookstore.bookstore.enums.Role;
import com.bookstore.bookstore.model.User;
import com.bookstore.bookstore.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class GoogleOAuth2SuccessHandler
        implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public GoogleOAuth2SuccessHandler(
            UserRepository userRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oauth2User =
                (OAuth2User) authentication.getPrincipal();

        String email =
                oauth2User.getAttribute("email");

        String firstName =
                oauth2User.getAttribute("given_name");

        String lastName =
                oauth2User.getAttribute("family_name");

        if (email == null) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Google email not found"
            );
            return;
        }

        User user =
                userRepository.findByEmail(email)
                        .orElseGet(() -> {

                            User newUser = new User();

                            newUser.setEmail(email);
                            newUser.setFirstName(
                                    firstName != null
                                            ? firstName
                                            : "Google"
                            );
                            newUser.setLastName(
                                    lastName != null
                                            ? lastName
                                            : "User"
                            );

                            // Google user doesn't need local password
                            newUser.setPassword(
                                    passwordEncoder.encode(
                                            UUID.randomUUID().toString()
                                    )
                            );

                            newUser.setRole(Role.CUSTOMER);

                            // Google already verifies email
                            newUser.setVerified(true);

                            return userRepository.save(newUser);
                        });

        // Generate JWT
        String token =
                jwtService.generateToken(
                        user.getEmail(),
                        false
                );

        // Temporary frontend redirect
        response.sendRedirect(
                "http://localhost:4200/oauth2/success?token="
                        + token
        );
    }
}