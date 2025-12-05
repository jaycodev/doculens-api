package com.doculens.tag.controller;

import com.doculens.shared.api.ApiSuccess;
import com.doculens.shared.validation.ValidationMessages;
import com.doculens.tag.dto.request.CreateTagRequest;
import com.doculens.tag.dto.request.UpdateTagRequest;
import com.doculens.tag.dto.response.TagDetailResponse;
import com.doculens.tag.dto.response.TagListResponse;
import com.doculens.tag.service.TagService;
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
@RequestMapping("/tags")
@RequiredArgsConstructor
@Validated
@Tag(name = "Tags", description = "Operations related to user tags")
public class TagController {
    private final TagService tagService;

    @GetMapping
    @Operation(summary = "List tags by user")
    public ResponseEntity<ApiSuccess<List<TagListResponse>>> list(
            @RequestParam Long userId) {
        List<TagListResponse> tags = tagService.getList(userId);
        ApiSuccess<List<TagListResponse>> response = new ApiSuccess<>(
                tags.isEmpty() ? "No tags found" : "Tags listed successfully",
                tags);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a tag by ID")
    public ResponseEntity<ApiSuccess<TagDetailResponse>> get(
            @PathVariable @Min(value = 1, message = ValidationMessages.ID_MIN_VALUE) Long id) {
        TagDetailResponse tag = tagService.getDetailById(id);
        return ResponseEntity.ok(new ApiSuccess<>("Tag found", tag));
    }

    @PostMapping
    @Operation(summary = "Create a new tag for a user")
    public ResponseEntity<ApiSuccess<TagListResponse>> create(
            @Valid @RequestBody CreateTagRequest request) {
        TagListResponse created = tagService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiSuccess<>("Tag created successfully", created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a tag by ID")
    public ResponseEntity<ApiSuccess<TagListResponse>> update(
            @PathVariable @Min(value = 1, message = ValidationMessages.ID_MIN_VALUE) Long id,
            @Valid @RequestBody UpdateTagRequest request) {
        TagListResponse result = tagService.update(id, request);
        return ResponseEntity.ok(new ApiSuccess<>("Tag updated successfully", result));
    }
}
