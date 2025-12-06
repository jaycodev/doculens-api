package com.doculens.folder.dto.response;

import java.time.LocalDateTime;

public record FolderListResponse(
        Long id,
        String name,
        Long parentId,
        LocalDateTime createdAt) {
}