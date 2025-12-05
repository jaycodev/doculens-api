package com.doculens.user.controller;

import com.doculens.shared.api.ApiSuccess;
import com.doculens.shared.validation.ValidationMessages;
import com.doculens.user.dto.request.CreateUserRequest;
import com.doculens.user.dto.request.UpdateUserRequest;
import com.doculens.user.dto.response.UserDetailResponse;
import com.doculens.user.dto.response.UserListResponse;
import com.doculens.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "Users", description = "Operations related to users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "List all users")
    public ResponseEntity<ApiSuccess<List<UserListResponse>>> list() {
        List<UserListResponse> users = userService.getList();
        ApiSuccess<List<UserListResponse>> response = new ApiSuccess<>(
                users.isEmpty() ? "No users found" : "Users listed successfully",
                users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by ID")
    public ResponseEntity<ApiSuccess<UserDetailResponse>> get(
            @PathVariable @Min(value = 1, message = ValidationMessages.ID_MIN_VALUE) Long id) {
        UserDetailResponse user = userService.getDetailById(id);
        return ResponseEntity.ok(new ApiSuccess<>("User found", user));
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<ApiSuccess<UserListResponse>> create(@Valid @RequestBody CreateUserRequest request) {
        UserListResponse created = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiSuccess<>("User created successfully", created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user by ID")
    public ResponseEntity<ApiSuccess<UserListResponse>> update(
            @PathVariable @Min(value = 1, message = ValidationMessages.ID_MIN_VALUE) Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        UserListResponse result = userService.update(id, request);
        return ResponseEntity.ok(new ApiSuccess<>("User updated successfully", result));
    }
}
