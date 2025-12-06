package com.doculens.document.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record AssignTagsRequest(
        @NotNull Long documentId,
        @NotEmpty Set<Long> tagIds) {
}