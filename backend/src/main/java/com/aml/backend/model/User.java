package com.aml.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String surname;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "phone_number")
    private String phone;

    private String sex;

    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(unique = true, nullable = false)
    private String nationalId;

    @Column(name = "account_number")
    private String accountNumber; // New field
}