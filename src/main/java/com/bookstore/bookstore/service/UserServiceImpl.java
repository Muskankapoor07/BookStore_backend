package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.AuthResponse;
import com.bookstore.bookstore.dto.UserLoginRequest;
import com.bookstore.bookstore.dto.UserRegistrationRequest;
import com.bookstore.bookstore.dto.UserResponse;
import com.bookstore.bookstore.dto.UserUpdateRequest;
import com.bookstore.bookstore.enums.Role;
import com.bookstore.bookstore.exception.ResourceAlreadyExistsException;
import com.bookstore.bookstore.mapper.UserMapper;
import com.bookstore.bookstore.model.User;
import com.bookstore.bookstore.repository.UserRepository;
import com.bookstore.bookstore.security.JwtService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    // ================= REGISTER =================

    @Override
    public UserResponse register(UserRegistrationRequest request) {

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
                passwordEncoder.encode(request.getPassword())
        );

        // Normal registration creates CUSTOMER
        user.setRole(Role.CUSTOMER);

        // Generate verification token
        String verificationToken = UUID.randomUUID().toString();

        user.setVerificationToken(verificationToken);
        user.setVerified(false);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    // ================= LOGIN =================

    @Override
    public AuthResponse login(UserLoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        String token = jwtService.generateToken(
                user.getEmail()
        );

        UserResponse userResponse =
                userMapper.toResponse(user);

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }

    // ================= VERIFY USER =================

    @Override
    public void verifyUser(String token) {

        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid verification token"
                        )
                );

        user.setVerified(true);
        user.setVerificationToken(null);

        userRepository.save(user);
    }

    // ================= UPDATE USER =================

    @Override
    public UserResponse updateUser(UserUpdateRequest request) {

        String currentEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }
}