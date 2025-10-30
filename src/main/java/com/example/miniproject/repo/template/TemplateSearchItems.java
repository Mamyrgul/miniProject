package com.example.miniproject.repo.template;

import com.example.miniproject.dto.response.AllItemUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
@RequiredArgsConstructor
public class TemplateSearchItems {
    private final JdbcTemplate jdbcTemplate;

    public List<AllItemUserResponse> findAndFilterItems(
            Long userId, String search, int page, int size
    ) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                i.id,
                i.name,
                i.description,
                i.address,
                CASE WHEN si.id IS NOT NULL THEN TRUE ELSE FALSE END AS is_saved,
                (
                    SELECT STRING_AGG(ii.image_urls, ', ')
                    FROM item_image_urls ii
                    WHERE ii.item_id = i.id
                ) AS image_urls
            FROM items i
            LEFT JOIN saved_items si
                   ON si.item_id = i.id
                  AND si.user_id = ?
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (search != null && !search.isBlank()) {
            String p = "%" + search.trim().toUpperCase() + "%";
            sql.append("""
                AND (
                    UPPER(i.address) LIKE ?
                    OR UPPER(i.name) LIKE ?
                    OR UPPER(i.description) LIKE ?
                )
            """);
            params.add(p);
            params.add(p);
            params.add(p);
        }

        int offset = Math.max(0, (page - 1) * size);
        sql.append(" ORDER BY i.id DESC LIMIT ? OFFSET ? ");
        params.add(size);
        params.add(offset);

        return jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) -> {
            String imageUrlsStr = rs.getString("image_urls");
            List<String> imageUrls = (imageUrlsStr != null && !imageUrlsStr.isBlank())
                    ? Arrays.asList(imageUrlsStr.split(",\\s*"))
                    : new ArrayList<>();

            return AllItemUserResponse.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .address(rs.getString("address"))
                    .isSaved(rs.getBoolean("is_saved")) // <-- имя алиаса
                    .imageUrls(imageUrls)
                    .build();
        });
    }
}
