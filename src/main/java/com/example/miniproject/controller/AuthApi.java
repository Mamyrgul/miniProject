package com.example.miniproject.controller;

import com.example.miniproject.dto.request.SignInRequest;
import com.example.miniproject.dto.request.SignUpRequest;
import com.example.miniproject.dto.response.AuthResponse;
import com.example.miniproject.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600, origins = "*")
@RequestMapping("/api/auth")
public class AuthApi {
    private final AuthService authService;

    @Operation(
            summary = "Регистрация",
            description = "Регистрирует нового пользователя на платформе. Требуется передать имя, email и пароль."
    )
    @PostMapping("/register")
    public AuthResponse signup(@RequestBody @Valid SignUpRequest signUpRequest) {
        return authService.signUp(signUpRequest);
    }

    @Operation(
            summary = "Вход",
            description = "Авторизует пользователя по email и паролю. Возвращает JWT токен при успешной аутентификации."
    )
    @PostMapping("/login")
    public AuthResponse signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return authService.signIn(signInRequest);
    }
}