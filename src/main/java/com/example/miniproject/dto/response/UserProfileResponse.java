package com.example.miniproject.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record UserProfileResponse(
        Long id,
        String fullName,
        String profileImage,
        List<AllItemUserResponse> myItems,
        List<AllItemUserResponse> savedItems
) {}

