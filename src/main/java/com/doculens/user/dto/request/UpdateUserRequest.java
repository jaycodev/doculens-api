package com.doculens.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    String email,

    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    String password
) {}
