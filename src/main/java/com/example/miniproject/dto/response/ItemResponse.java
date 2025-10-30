package com.example.miniproject.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ItemResponse(
        Long id,
        String name,
        String description,
        List<String> imageUrls,
        Long userId,
        String address
) {
}
