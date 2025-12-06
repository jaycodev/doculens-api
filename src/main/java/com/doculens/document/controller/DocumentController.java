package com.doculens.document.controller;

import com.doculens.document.dto.request.AssignTagsRequest;
import com.doculens.document.dto.request.CreateDocumentRequest;
import com.doculens.document.dto.request.UpdateDocumentRequest;
import com.doculens.document.dto.response.DocumentDetailResponse;
import com.doculens.document.dto.response.DocumentListResponse;
import com.doculens.document.service.DocumentService;
import com.doculens.shared.api.ApiSuccess;

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
@RequestMapping("/documents")
@RequiredArgsConstructor
@Validated
@Tag(name = "Documents", description = "Operations related to documents")
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    @Operation(summary = "List documents by user and folder")
    public ResponseEntity<ApiSuccess<List<DocumentListResponse>>> list(
            @RequestParam Long userId,
            @RequestParam(required = false) Long folderId) {
        var list = documentService.getList(userId, folderId);
        return ResponseEntity.ok(new ApiSuccess<>("Documents listed", list));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a document by ID")
    public ResponseEntity<ApiSuccess<DocumentDetailResponse>> get(
            @PathVariable @Min(1) Long id) {
        var detail = documentService.getDetailById(id);
        return ResponseEntity.ok(new ApiSuccess<>("Document found", detail));
    }

    @PostMapping
    @Operation(summary = "Create a new document")
    public ResponseEntity<ApiSuccess<DocumentDetailResponse>> create(
            @Valid @RequestBody CreateDocumentRequest request) {
        var created = documentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiSuccess<>("Document created", created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing document")
    public ResponseEntity<ApiSuccess<DocumentDetailResponse>> update(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody UpdateDocumentRequest request) {
        var updated = documentService.update(id, request);
        return ResponseEntity.ok(new ApiSuccess<>("Document updated", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a document by ID")
    public ResponseEntity<ApiSuccess<Void>> delete(
            @PathVariable @Min(1) Long id) {
        documentService.delete(id);
        return ResponseEntity.ok(new ApiSuccess<>("Document deleted", null));
    }

    // Tags
    @PutMapping("/{id}/tags")
    @Operation(summary = "Add tags to a document")
    public ResponseEntity<ApiSuccess<DocumentDetailResponse>> addTags(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody AssignTagsRequest request) {

        var updated = documentService.addTags(id, request.tagIds());
        return ResponseEntity.ok(new ApiSuccess<>("Tags assigned", updated));
    }

    @DeleteMapping("/{id}/tags/{tagId}")
    @Operation(summary = "Remove a tag from a document")
    public ResponseEntity<ApiSuccess<DocumentDetailResponse>> removeTag(
            @PathVariable @Min(1) Long id,
            @PathVariable @Min(1) Long tagId) {

        var updated = documentService.removeTag(id, tagId);
        return ResponseEntity.ok(new ApiSuccess<>("Tag removed", updated));
    }

    @PutMapping("/{id}/tags/replace")
    @Operation(summary = "Replace tags of a document")
    public ResponseEntity<ApiSuccess<DocumentDetailResponse>> replaceTags(
            @PathVariable @Min(1) Long id,
            @RequestBody AssignTagsRequest request) {

        var updated = documentService.replaceTags(id, request.tagIds());
        return ResponseEntity.ok(new ApiSuccess<>("Tags replaced", updated));
    }
}