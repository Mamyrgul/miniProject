package com.example.miniproject.controller;

import com.example.miniproject.dto.response.SimpleResponse;
import com.example.miniproject.service.SaveItemsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600, origins = "*")
@RequestMapping("/api/favorite")
public class SavedApi {
    private final SaveItemsService saveItemsService;

    @Operation(
            summary = "Добавить или удалить из избранного",
            description = "Добавляет дом в избранное, если его там нет, или удаляет, если уже добавлен."
    )
    @PostMapping("/action")
    public SimpleResponse save(Long itemId) {
        return saveItemsService.addToSaved(itemId);
    }

}
