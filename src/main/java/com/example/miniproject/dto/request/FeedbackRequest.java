package com.example.miniproject.dto.request;

import java.time.LocalDate;

public record FeedbackRequest(
        String text,
        LocalDate createdAt,
        Boolean isLiked
){
}
