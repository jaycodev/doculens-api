package com.doculens.document.dto.response;

import java.time.LocalDateTime;

public record DocumentListResponse(
        Long id,
        String title,
        Long folderId,
        String mimeType,
        LocalDateTime createdAt) {
}