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
            Long userId,
            String search,
            int page,
            int size
    ) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
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
                                ) AS image_urls
                            FROM items i
                            WHERE 1=1
                
                """);

        params.add(userId);

        if (search != null && !search.isBlank()) {
            String searchPattern = "%" + search.trim().toUpperCase() + "%";
            sql.append("""
                    AND (
                        UPPER(i.address) LIKE ?
                        OR UPPER(i.name) LIKE ?
                        OR UPPER(i.type) LIKE ?
                    )
                    """);
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }

        int offset = (page - 1) * size;
        sql.append(" ORDER BY i.id DESC LIMIT ? OFFSET ? ");
        params.add(size);
        params.add(offset);

        return jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) -> {
            String imageUrlsStr = rs.getString("image_urls");
            List<String> imageUrls = (imageUrlsStr != null && !imageUrlsStr.isEmpty())
                    ? Arrays.asList(imageUrlsStr.split(",\\s*"))
                    : new ArrayList<>();

            return AllItemUserResponse.builder()
                    .id(rs.getLong("id"))
                    .price(rs.getDouble("price"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .address(rs.getString("address"))
                    .imageUrls(imageUrls)
                    .build();
        });
    }
}
