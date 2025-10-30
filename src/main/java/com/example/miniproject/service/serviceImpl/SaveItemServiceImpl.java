package com.example.miniproject.service.serviceImpl;

import com.example.miniproject.dto.response.AllItemUserResponse;
import com.example.miniproject.dto.response.SimpleResponse;
import com.example.miniproject.entity.Item;
import com.example.miniproject.entity.SaveItems;
import com.example.miniproject.entity.User;
import com.example.miniproject.exception.ForbiddenException;
import com.example.miniproject.exception.NotFoundException;
import com.example.miniproject.repo.ItemRepo;
import com.example.miniproject.repo.SaveItemsRepo;
import com.example.miniproject.repo.UserRepository;
import com.example.miniproject.repo.template.TemplateSavedItemRepo;
import com.example.miniproject.service.SaveItemsService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Builder
@Transactional
public class SaveItemServiceImpl implements SaveItemsService {
    private final SaveItemsRepo saveItemsRepo;
    private final TemplateSavedItemRepo templateSavedItemRepo;
    private final UserRepository userRepo;
    private final ItemRepo itemRepo;

    @Override
    public SimpleResponse addToSaved(Long itemId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email: " + email + " not found"));

        Item item = itemRepo.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id %s not found", itemId)));

        Optional<SaveItems> existing = saveItemsRepo.findByUserAndItem(user, item);

        if (existing.isEmpty()) {
            SaveItems saveItem = SaveItems.builder()
                    .user(user)
                    .item(item)
                    .addedAt(LocalDate.now())
                    .build();

            saveItemsRepo.save(saveItem);

            return SimpleResponse.builder()
                    .message("✅ Item with id " + itemId + " added successfully to favorites")
                    .httpStatus(HttpStatus.OK)
                    .build();
        } else {
            saveItemsRepo.delete(existing.get());

            return SimpleResponse.builder()
                    .message("Item with id " + itemId + " removed successfully from favorites")
                    .httpStatus(HttpStatus.OK)
                    .build();
        }
    }

    public List<AllItemUserResponse> getSaved() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepo.findByEmail(email).orElseThrow(
                () -> new NotFoundException("User with email " + email + " not found"));
        if (!email.equals(user.getEmail())) {
            throw new ForbiddenException("You are not allowed to access this saved items");
        }
        return templateSavedItemRepo.getSaved(user.getId());
    }

    @Override
    public AllItemUserResponse getSavedById(Long itemId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepo.findByEmail(email).orElseThrow(
                () -> new NotFoundException("User with email " + email + " not found"));
        if (!email.equals(user.getEmail())) {
            throw new ForbiddenException("You are not allowed to access this saved item");
        }
        return templateSavedItemRepo.getSaveItemById(user.getId(), itemId);
    }
}
