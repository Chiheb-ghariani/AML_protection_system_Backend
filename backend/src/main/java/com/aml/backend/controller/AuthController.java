package com.aml.backend.controller;

import com.aml.backend.dto.RegisterRequest;
import com.aml.backend.model.Role;
import com.aml.backend.model.User;
import com.aml.backend.repository.UserRepository;
import com.aml.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        System.out.println("📌 Incoming request: " + request);

        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setSex(request.getSex());
        user.setRole(request.getRole() != null ? request.getRole() : Role.CUSTOMER);

        if (request.getBirthdate() != null) {
            try {
                user.setBirthdate(LocalDate.parse(request.getBirthdate()));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Invalid birthdate format. Use YYYY-MM-DD");
            }
        }

        try {
            userRepository.save(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role specified");
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody User loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return jwtUtil.generateToken(user.getEmail());
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}