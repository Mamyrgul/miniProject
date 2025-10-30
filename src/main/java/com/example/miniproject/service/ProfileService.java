package com.example.miniproject.service;

import com.example.miniproject.dto.response.UserProfileResponse;
import org.springframework.stereotype.Service;

@Service
public interface ProfileService {
    UserProfileResponse getCurrentUserProfile();
}
