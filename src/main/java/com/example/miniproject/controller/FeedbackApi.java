package com.example.miniproject.controller;

import com.example.miniproject.dto.request.FeedbackRequest;
import com.example.miniproject.dto.response.SimpleResponse;
import com.example.miniproject.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600, origins = "*")
@RequestMapping("/api/feedback")
public class FeedbackApi {
    private final FeedbackService feedbackService;

    @Operation(
            summary = "Сохранить отзыв",
            description = "Добавляет новый отзыв и рейтинг для указанного дома. Требует ID дома, текст отзыва и оценку."
    )
    @PostMapping("/save/{itemId}")
    public void save(@PathVariable Long itemId, @RequestBody FeedbackRequest feedbackRequest) {
         feedbackService.saveFeedback(itemId,feedbackRequest);
    }

    @Operation(
            summary = "Удалить отзыв по ID",
            description = "Удаляет отзыв по его уникальному идентификатору."
    )
    @DeleteMapping("/delete/{id}")
    public SimpleResponse deleteById(@PathVariable Long id) {
        return feedbackService.deleteFeedback(id);
    }
}
