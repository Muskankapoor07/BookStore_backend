package com.bookstore.bookstore.service;

import com.bookstore.bookstore.dto.AuthResponse;
import com.bookstore.bookstore.dto.CustomerDetailsRequest;
import com.bookstore.bookstore.dto.ForgotPasswordRequest;
import com.bookstore.bookstore.dto.ResetPasswordRequest;
import com.bookstore.bookstore.dto.UserLoginRequest;
import com.bookstore.bookstore.dto.UserRegistrationRequest;
import com.bookstore.bookstore.dto.UserResponse;
import com.bookstore.bookstore.dto.UserUpdateRequest;
import com.bookstore.bookstore.enums.Role;
import com.bookstore.bookstore.exception.ResourceAlreadyExistsException;
import com.bookstore.bookstore.exception.ResourceNotFoundException;
import com.bookstore.bookstore.mapper.UserMapper;
import com.bookstore.bookstore.messaging.rabbitmq.producer.PasswordResetProducer;
import com.bookstore.bookstore.model.User;
import com.bookstore.bookstore.repository.UserRepository;
import com.bookstore.bookstore.security.JwtService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final StringRedisTemplate stringRedisTemplate;
    private final PasswordResetProducer passwordResetProducer;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            JwtService jwtService,
            StringRedisTemplate stringRedisTemplate,
            PasswordResetProducer passwordResetProducer) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.passwordResetProducer = passwordResetProducer;
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

        user.setRole(Role.CUSTOMER);

        String verificationToken =
                UUID.randomUUID().toString();

        user.setVerificationToken(verificationToken);
        user.setVerified(false);

        User savedUser =
                userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    // ================= LOGIN =================

    @Override
    public AuthResponse login(UserLoginRequest request) {

        User user =
                userRepository.findByEmail(request.getEmail())
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

    // ================= VERIFY USER =================

    @Override
    public void verifyUser(String token) {

        User user =
                userRepository.findByVerificationToken(token)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid verification token"
                                )
                        );

        user.setVerified(true);
        user.setVerificationToken(null);

        userRepository.save(user);
    }

    // ================= GET USER PROFILE =================

    @Override
    public UserResponse getProfile(String email) {

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        return userMapper.toResponse(user);
    }

    // ================= UPDATE USER =================

    @Override
    public UserResponse updateUser(
            UserUpdateRequest request) {

        String currentEmail =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user =
                userRepository.findByEmail(currentEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());

        User updatedUser =
                userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    // ================= UPDATE CUSTOMER DETAILS =================

    @Override
    public UserResponse updateCustomerDetails(
            CustomerDetailsRequest request) {

        String currentEmail =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user =
                userRepository.findByEmail(currentEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        user.setAddressType(request.getAddressType());
        user.setFullAddress(request.getFullAddress());
        user.setCity(request.getCity());
        user.setState(request.getState());

        User updatedUser =
                userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    // ================= FORGOT PASSWORD =================

    @Override
    public void forgotPassword(
            ForgotPasswordRequest request) {

        User user =
                userRepository.findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with this email"
                                )
                        );

        String resetToken =
                UUID.randomUUID().toString();

        String redisKey =
                "reset-password:" + resetToken;

        stringRedisTemplate.opsForValue().set(
                redisKey,
                user.getEmail(),
                10,
                TimeUnit.MINUTES
        );

        passwordResetProducer.sendPasswordResetEvent(
                user.getEmail(),
                resetToken
        );
    }

    // ================= RESET PASSWORD =================

    @Override
    public void resetPassword(
            ResetPasswordRequest request) {

        String redisKey =
                "reset-password:" + request.getToken();

        String email =
                stringRedisTemplate.opsForValue()
                        .get(redisKey);

        if (email == null) {
            throw new RuntimeException(
                    "Invalid or expired reset token"
            );
        }

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);

        stringRedisTemplate.delete(redisKey);
    }
}