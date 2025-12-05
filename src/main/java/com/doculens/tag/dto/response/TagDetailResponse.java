package com.doculens.tag.dto.response;

import java.time.LocalDateTime;

public record TagDetailResponse(
    Long id,
    String name,
    Long userId,
    LocalDateTime createdAt
) {}
