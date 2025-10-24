package com.example.miniproject.dto.response;

import com.example.miniproject.enums.Role;
import lombok.Builder;

@Builder
public record AuthResponse(
        Long id,
        String email,
        String token,
        Role role
) {
}
