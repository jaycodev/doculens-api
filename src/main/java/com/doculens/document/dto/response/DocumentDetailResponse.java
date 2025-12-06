package com.doculens.document.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

public record DocumentDetailResponse(
        Long id,
        String title,
        String fileUrl,
        String extractedFields,
        String originalFilename,
        String mimeType,
        Long folderId,
        Long userId,
        Set<Long> tagIds,
        LocalDateTime createdAt) {
}