package com.doculens.sync.dto.request;

import jakarta.validation.constraints.NotNull;

public record SyncEventRequest(
        @NotNull Long userId,
        @NotNull String entityType,
        @NotNull Long entityId,
        @NotNull String operation) {
}