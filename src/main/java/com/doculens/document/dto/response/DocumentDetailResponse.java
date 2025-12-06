package com.doculens.document.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record DocumentDetailResponse(
        Long id,
        String title,
        String fileUrl,
        Map<String, Object> extractedFields,
        String originalFilename,
        String mimeType,
        Long folderId,
        Long userId,
        List<Long> tagIds,
        LocalDateTime createdAt) {
}