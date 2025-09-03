package com.aml.backend.controller;

import com.aml.backend.dto.RegisterRequest;
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

    // Register new user (signup) – hashes password
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

        if (request.getBirthdate() != null) {
            try {
                user.setBirthdate(LocalDate.parse(request.getBirthdate()));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Invalid birthdate format. Use YYYY-MM-DD");
            }
        }

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }



    // Login – checks hashed password and returns JWT (as plain text)
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
