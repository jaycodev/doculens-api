package com.doculens.tag.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTagRequest(
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must not exceed 50 characters")
    String name,

    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be greater than 0")
    Long userId
) {}
