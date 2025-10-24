package com.example.miniproject.dto.response;

import java.util.List;

public record CombinedItemsResponse(
        List<AllItemUserResponse> allItems
) {}
