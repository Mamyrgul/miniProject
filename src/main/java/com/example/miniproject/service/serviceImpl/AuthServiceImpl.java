package com.example.miniproject.service.serviceImpl;

import com.example.miniproject.config.jwt.JwtService;
import com.example.miniproject.dto.request.SignInRequest;
import com.example.miniproject.dto.request.SignUpRequest;
import com.example.miniproject.dto.response.AuthResponse;
import com.example.miniproject.entity.User;
import com.example.miniproject.enums.Role;
import com.example.miniproject.exception.AlreadyExist;
import com.example.miniproject.exception.NotFoundException;
import com.example.miniproject.repo.UserRepository;
import com.example.miniproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, String> tokenStorage = new HashMap<>();

    @Override
    public AuthResponse signUp(SignUpRequest signUpRequest) {
        if (!signUpRequest.password().equals(signUpRequest.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (userRepo.existsByEmail(signUpRequest.email())) {
            throw new AlreadyExist("Email already exists");
        }

        User user = User.builder()
                .fullName(signUpRequest.fullName())
                .phoneNumber(signUpRequest.phoneNumber())
                .email(signUpRequest.email())
                .password(passwordEncoder.encode(signUpRequest.password()))
                .role(Role.USER)
                .build();

        userRepo.save(user);

        return AuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .token(jwtService.generateToken(user))
                .role(user.getRole())
                .build();
    }
    @Override
    public AuthResponse signIn(SignInRequest signInRequest) {
        User user = userRepo.findByEmail(signInRequest.email())
                .orElseThrow(() -> new NotFoundException("User with email " + signInRequest.email() + " not found!"));
        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return AuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .token(jwtService.generateToken(user))
                .role(user.getRole())
                .build();
    }
}