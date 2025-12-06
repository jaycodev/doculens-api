package com.doculens.sync.dto.response;

import java.time.LocalDateTime;

public record SyncEventResponse(
        Long id,
        String entityType,
        Long entityId,
        String operation,
        LocalDateTime timestamp) {
}