package com.example.miniproject.service;
import com.example.miniproject.dto.request.SignInRequest;
import com.example.miniproject.dto.request.SignUpRequest;
import com.example.miniproject.dto.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    AuthResponse signUp(SignUpRequest signUpRequest);
    AuthResponse signIn(SignInRequest signInRequest);
}
