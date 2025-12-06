package com.doculens.folder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateFolderRequest(
        @NotNull Long userId,
        Long parentId,
        @NotBlank @Size(max = 255) String name) {
}