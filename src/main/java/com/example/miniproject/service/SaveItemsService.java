package com.example.miniproject.service;

import com.example.miniproject.dto.response.AllItemUserResponse;
import com.example.miniproject.dto.response.SimpleResponse;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface SaveItemsService {
    SimpleResponse addToSaved(Long houseId);
    List<AllItemUserResponse> getSaved();
    AllItemUserResponse getSavedById(Long itemId);
}
