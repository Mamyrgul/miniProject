package com.example.miniproject.repo.template;

import com.example.miniproject.dto.response.AllItemUserResponse;
import com.example.miniproject.exception.NotFoundException;
import lombok.Builder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Builder
public class TemplateSavedItemRepo {
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public List<AllItemUserResponse> getSaved(Long userId) {
        String sql = """
             SELECT
                i.id,
                i.price,
                i.name,
                i.description,
                i.address,
                    SELECT STRING_AGG(ii.image_urls, ', ')
                    FROM item_image_urls ii
                    WHERE ii.item_id = i.id
                ) AS image_urls,
                EXISTS (
                    SELECT 1
                    FROM saved_items si
                    WHERE si.item_id = i.id AND si.user_id = ?
                ) AS saved_items
            FROM items i
            WHERE i.id = ?
            """;

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            String imageUrlsStr = rs.getString("image_urls");
            List<String> images = (imageUrlsStr != null && !imageUrlsStr.isBlank())
                    ? Arrays.asList(imageUrlsStr.split(",\\s*"))
                    : new ArrayList<>();

            return AllItemUserResponse.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .price(rs.getDouble("price"))
                    .address(rs.getString("address"))
                    .isSaved(rs.getBoolean("saved_items"))
                    .imageUrls(images)
                    .build();
        });
    }

    public AllItemUserResponse getSaveItemById(Long userId, Long itemId) {
        String sql = """
        SELECT
            i.id,
            i.price,
            i.name,
            i.description,
            i.address,
            (
                SELECT STRING_AGG(ii.image_url, ', ')
                FROM item_image_urls ii
                WHERE ii.item_id = i.id
            ) AS image_urls,
            EXISTS (
                SELECT 1
                FROM saved_items si
                WHERE si.item_id = i.id AND si.user_id = ?
            ) AS saved_items
        FROM items i
        WHERE i.id = ?
    """;

        return jdbcTemplate.query(sql, new Object[]{userId, itemId}, rs -> {
            if (!rs.next()) {
                throw new NotFoundException("Saved item not found!");
            }

            String imageUrlsStr = rs.getString("image_urls");
            List<String> images = (imageUrlsStr != null && !imageUrlsStr.isBlank())
                    ? Arrays.asList(imageUrlsStr.split(",\\s*"))
                    : new ArrayList<>();

            return AllItemUserResponse.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .price(rs.getDouble("price"))
                    .address(rs.getString("address"))
                    .isSaved(rs.getBoolean("saved_items"))
                    .imageUrls(images)
                    .build();
        });
    }
}