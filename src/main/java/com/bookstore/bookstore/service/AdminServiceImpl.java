package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.AdminLoginRequest;
import com.bookstore.bookstore.dto.AdminRegistrationRequest;
import com.bookstore.bookstore.dto.AuthResponse;
import com.bookstore.bookstore.dto.UserResponse;
import com.bookstore.bookstore.enums.Role;
import com.bookstore.bookstore.exception.ResourceAlreadyExistsException;
import com.bookstore.bookstore.mapper.UserMapper;
import com.bookstore.bookstore.model.User;
import com.bookstore.bookstore.repository.UserRepository;
import com.bookstore.bookstore.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public AdminServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    // ================= REGISTER ADMIN =================

    @Override
    public UserResponse registerAdmin(
            AdminRegistrationRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException(
                    "Email already registered"
            );
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(Role.ADMIN);

        User savedUser =
                userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    // ================= ADMIN LOGIN =================

    @Override
    public AuthResponse loginAdmin(
            AdminLoginRequest request) {

        User user =
                userRepository.findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid email or password"
                                )
                        );

        // Check ADMIN role
        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException(
                    "Access denied. Admin account required."
            );
        }

        // Check password
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        // Remember Me decides token expiry
        String token =
                jwtService.generateToken(
                        user.getEmail(),
                        request.isRememberMe()
                );

        UserResponse userResponse =
                userMapper.toResponse(user);

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }
}