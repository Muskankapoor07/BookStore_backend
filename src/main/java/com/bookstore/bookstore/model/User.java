package com.bookstore.bookstore.model;

import com.bookstore.bookstore.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String mobileNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // ================= USER VERIFICATION =================

    @Column
    private String verificationToken;

    @Column(nullable = false)
    @Builder.Default
    private boolean verified = false;

    // ================= PASSWORD RESET =================

    @Column
    private String resetPasswordToken;

    // ================= CUSTOMER ADDRESS =================

    @Column
    private String addressType;

    @Column
    private String fullAddress;

    @Column
    private String city;

    @Column
    private String state;
}