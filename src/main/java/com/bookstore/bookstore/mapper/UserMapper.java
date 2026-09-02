package com.bookstore.bookstore.mapper;

import com.bookstore.bookstore.dto.UserResponse;
import com.bookstore.bookstore.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .addressType(user.getAddressType())
                .fullAddress(user.getFullAddress())
                .city(user.getCity())
                .state(user.getState())
                .role(user.getRole())
                .build();
    }
}