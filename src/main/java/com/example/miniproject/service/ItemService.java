package com.example.miniproject.service;

import com.example.miniproject.dto.request.ItemCreationRequest;
import com.example.miniproject.dto.request.ItemUpdateRequest;
import com.example.miniproject.dto.response.CombinedItemsResponse;
import com.example.miniproject.dto.response.ItemResponse;
import com.example.miniproject.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ItemService {
    ResponseEntity<?> addItem(ItemCreationRequest itemCreationRequest);
    ResponseEntity<?> updateItem(ItemUpdateRequest itemUpdateRequest);
    ItemResponse getItemById(Long itemId);
    CombinedItemsResponse getCombinedItems(Long userId, int page, int size, String search);
    String deleteItem(Long itemId, User userDetails);
}
