package com.doculens.folder.dto.response;

import java.time.LocalDateTime;

public record FolderDetailResponse(
        Long id,
        String name,
        Long parentId,
        Long userId,
        LocalDateTime createdAt) {
}