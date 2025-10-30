package com.example.miniproject.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record ItemUpdateRequest(
        Long houseId,
        @NotBlank(message = "Name is required")
        String name,

        @Size(max = 1000, message = "Description is too long")
        String description,

        @NotBlank(message = "City is required")
        String city,

        @NotBlank(message = "Address is required")
        String address,

        @NotNull(message = "Image URLs are required")
        @Size(min = 1, message = "At least one image is required")
        List<@NotBlank(message = "Image URL cannot be blank") String> imageUrls
) {}
