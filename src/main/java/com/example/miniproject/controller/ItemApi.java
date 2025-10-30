package com.example.miniproject.controller;

import com.example.miniproject.dto.request.ItemCreationRequest;
import com.example.miniproject.dto.request.ItemUpdateRequest;
import com.example.miniproject.dto.response.CombinedItemsResponse;
import com.example.miniproject.dto.response.ItemResponse;
import com.example.miniproject.entity.User;
import com.example.miniproject.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600, origins = "*")
@RequestMapping("/api/item")
public class ItemApi {

    private final ItemService itemService;

    @Operation(summary = "Получение информации о публикации по ID")
    @GetMapping("/get/{id}")
    public ItemResponse getHouse(@PathVariable("id") Long id) {
        return itemService.getItemById(id);
    }

    @Operation(summary = "Создание нового публикации")
    @PostMapping("/new")
    public ResponseEntity<?> addHouse(@RequestBody ItemCreationRequest itemCreationRequest){
        return itemService.addItem(itemCreationRequest);
    }

    @Operation(summary = "Обновление информации о публикации")
    @PutMapping("/update")
    public ResponseEntity<?> updateHouse(@RequestBody ItemUpdateRequest itemUpdateRequest){
        return itemService.updateItem(itemUpdateRequest);
    }

    @Operation(summary = "Получение списка вещей для пользователей с фильтрами, поиском")
    @Secured("USER")
    @GetMapping("/for-users")
    public ResponseEntity<CombinedItemsResponse> getCombinedHouses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "16") int size,
            @RequestParam(required = false) String search
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) auth.getPrincipal();
        Long userId = userDetails.getId();
        CombinedItemsResponse response = itemService.getCombinedItems(
                userId, page, size,
                search
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Удаление публикацию по ID (только для админа и пользователя кто публиковал)")
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<String> deleteHouse(
            @PathVariable Long itemId,
            @AuthenticationPrincipal User userDetails
    ) {
        String result = itemService.deleteItem(itemId, userDetails);
        return ResponseEntity.ok(result);
    }

}
