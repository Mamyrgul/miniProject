package com.example.miniproject.service;

import com.example.miniproject.dto.request.FeedbackRequest;
import com.example.miniproject.dto.response.SimpleResponse;
import org.springframework.stereotype.Service;

@Service
public interface FeedbackService {
    void saveFeedback(Long houseId, FeedbackRequest feedbackRequest);
    SimpleResponse deleteFeedback(Long feedbackId);
}
