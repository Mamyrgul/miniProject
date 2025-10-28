package com.example.miniproject.dto.response;

import java.util.List;

public record UserProfileResponse(
        String fullName,
        String profilImage,
        Long id,
        String name,
        String description,
        String address,
        boolean isSaved,
        List<String>imageUrls

) {
}
