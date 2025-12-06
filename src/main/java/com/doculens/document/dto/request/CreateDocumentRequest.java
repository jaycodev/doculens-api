package com.doculens.document.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateDocumentRequest(
        @NotNull Long userId,
        Long folderId,
        @NotBlank @Size(max = 255) String title,
        @NotBlank String fileUrl,
        String extractedFields,
        String originalFilename,
        String mimeType) {
}
