package com.doculens.tag.dto.response;

import java.time.LocalDateTime;

public record TagListResponse(
    Long id,
    String name,
    Long userId,
    LocalDateTime createdAt
) {}
