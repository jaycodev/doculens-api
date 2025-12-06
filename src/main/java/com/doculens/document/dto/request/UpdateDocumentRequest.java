package com.doculens.document.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record UpdateDocumentRequest(
        @NotBlank @Size(max = 255) String title,
        Long folderId,
        Map<String, Object> extractedFields) {
}