package com.doculens.folder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateFolderRequest(
        @NotBlank @Size(max = 255) String name) {
}
