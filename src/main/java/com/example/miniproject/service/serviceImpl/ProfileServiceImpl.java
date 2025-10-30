package com.example.miniproject.service.serviceImpl;

import com.example.miniproject.dto.response.AllItemUserResponse;
import com.example.miniproject.dto.response.UserProfileResponse;
import com.example.miniproject.entity.User;
import com.example.miniproject.repo.ItemRepo;
import com.example.miniproject.repo.UserRepository;
import com.example.miniproject.repo.template.TemplateSavedItemRepo;
import com.example.miniproject.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepo;
    private final ItemRepo itemRepo;
    private final TemplateSavedItemRepo templateSavedItemRepo;

    public UserProfileResponse getCurrentUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));

        // Собственные публикации
        List<AllItemUserResponse> myItems = user.getItems().stream()
                .map(item -> AllItemUserResponse.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .address(item.getAddress())
                        .isSaved(false)
                        .imageUrls(item.getImageUrls())  // ✅ вот так
                        .build())
                .collect(Collectors.toList());

        List<AllItemUserResponse> savedItems = templateSavedItemRepo.getSaved(user.getId());

        return UserProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .profileImage(user.getImageUrl())
                .myItems(myItems)
                .savedItems(savedItems)
                .build();
    }
}
