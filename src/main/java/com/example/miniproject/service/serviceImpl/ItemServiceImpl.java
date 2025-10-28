package com.example.miniproject.service.serviceImpl;

import com.example.miniproject.dto.request.ItemCreationRequest;
import com.example.miniproject.dto.request.ItemUpdateRequest;
import com.example.miniproject.dto.response.AllItemUserResponse;
import com.example.miniproject.dto.response.CombinedItemsResponse;
import com.example.miniproject.dto.response.ItemResponse;
import com.example.miniproject.enity.Item;
import com.example.miniproject.enity.User;
import com.example.miniproject.enums.Role;
import com.example.miniproject.exception.NotFoundException;
import com.example.miniproject.repo.ItemRepo;
import com.example.miniproject.repo.UserRepository;
import com.example.miniproject.repo.template.TemplateSearchItems;
import com.example.miniproject.service.ItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepo;
    private final ItemRepo itemRepo;
    private final JdbcTemplate jdbcTemplate;
    private final TemplateSearchItems templateSearchItems;

    @Transactional
    @Override
    public ResponseEntity<?> addItem(ItemCreationRequest itemCreationRequest) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByEmail(currentUserEmail).orElseThrow(
                () -> new NotFoundException("User with name: " + currentUserEmail + " not found"));
        Item item = Item.builder()
                .imageUrls(itemCreationRequest.imageUrls())
                .name(itemCreationRequest.name())
                .description(itemCreationRequest.description())
                .address(itemCreationRequest.address())
                .build();
        currentUser.getItems().add(item);
        item.setUser(currentUser);
        userRepo.save(currentUser);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of(
                        "message", "You successfully added a item!",
                        "status", "The item is under review!"
                ));
    }

    @Transactional
    @Override
    public ResponseEntity<?> updateItem(ItemUpdateRequest itemUpdateRequest) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Long> currentUserItemsId = jdbcTemplate.query(
                """
                        select i.id from items i 
                        join users u on i.user_id = u.id
                        where u.email = ?
                        """, (rs, i) -> rs.getLong("id"), currentUserEmail);

        if (currentUserItemsId.contains(itemUpdateRequest.houseId())) {
            Item item = itemRepo.findById(itemUpdateRequest.houseId()).orElseThrow(() -> new NotFoundException("Item with id: " + itemUpdateRequest.houseId() + " not found"));

            List<String> updatedImages = itemUpdateRequest.imageUrls();
            if (!updatedImages.isEmpty()) {
                item.getImageUrls().clear();
                item.setImageUrls(updatedImages);
            }

            item.setName(itemUpdateRequest.name());
            item.setDescription(itemUpdateRequest.description());

            itemRepo.save(item);
            return ResponseEntity.ok("The item has been updated!");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("You do not have permission to update this house. It may not belong to you.");
    }

    @Override
    public ItemResponse getItemById(Long itemId) {
        Item item = itemRepo.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id: " + itemId + " not found"));
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .imageUrls(item.getImageUrls())
                .address(item.getAddress())
                .userId(item.getUser() != null ? item.getUser().getId() : null)
                .build();
    }

    public CombinedItemsResponse getCombinedItems(
            Long userId,
            int page,
            int size,
            String search
    ) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(currentUserEmail).orElseThrow(
                ()->new NotFoundException("User with email: " + currentUserEmail + " not found"));
        List<AllItemUserResponse> result;
            result = templateSearchItems.findAndFilterItems(
                    user.getId(),
                    search,
                    page,
                    size
            );
        return new CombinedItemsResponse(result);
    }

    @Override
    @Transactional
    public String deleteItem(Long itemId, User userDetails) {
        User currentUser = userRepo.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));
        Item item = itemRepo.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
        boolean isOwner = item.getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new NotFoundException("You don't have permission to delete this item");
        }
        itemRepo.delete(item);
        return "Item deleted successfully";
    }
}
