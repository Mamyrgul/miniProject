package com.example.miniproject.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record ItemCreationRequest(
         List<String> imageUrls,
         @NotNull
         double price,
         String name,
         @Size(max = 1000)
         String description,
         @NotNull
         String region,
         @NotNull
         String city,
         @NotNull
         String address
){
}
