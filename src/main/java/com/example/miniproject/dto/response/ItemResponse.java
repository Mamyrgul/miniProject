package com.example.miniproject.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ItemResponse(
        Long id,
        String name,
        double price,
        String description,
        List<String> imageUrls,
        Long userId,
        String address
) {
}
