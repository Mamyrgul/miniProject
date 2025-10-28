package com.example.miniproject.dto.response;
import java.time.LocalDateTime;

public record MessageResponse(
        Long id,
        String content,
        String imageUrl,
        String linkUrl,
        String senderName,
        String receiverName,
        LocalDateTime timestamp,
        String status
) { }
