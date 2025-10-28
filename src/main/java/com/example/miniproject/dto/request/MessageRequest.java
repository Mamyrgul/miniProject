package com.example.miniproject.dto.request;
import lombok.Data;

@Data
public class MessageRequest {
    private Long receiverId;
    private String content;
    private String imageUrl;
    private String linkUrl;
}

