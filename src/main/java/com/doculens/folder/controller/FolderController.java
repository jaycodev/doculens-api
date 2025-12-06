package com.doculens.folder.controller;

import com.doculens.folder.dto.request.CreateFolderRequest;
import com.doculens.folder.dto.request.UpdateFolderRequest;
import com.doculens.folder.dto.response.FolderDetailResponse;
import com.doculens.folder.dto.response.FolderListResponse;
import com.doculens.folder.service.FolderService;
import com.doculens.shared.api.ApiSuccess;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/folders")
@RequiredArgsConstructor
@Validated
public class FolderController {

    private final FolderService folderService;

    @GetMapping
    public ResponseEntity<ApiSuccess<List<FolderListResponse>>> list(
            @RequestParam Long userId,
            @RequestParam(required = false) Long parentId) {
        var list = folderService.getList(userId, parentId);
        return ResponseEntity.ok(new ApiSuccess<>("Folders listed", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccess<FolderDetailResponse>> get(
            @PathVariable @Min(1) Long id) {
        var folder = folderService.getDetailById(id);
        return ResponseEntity.ok(new ApiSuccess<>("Folder found", folder));
    }

    @PostMapping
    public ResponseEntity<ApiSuccess<FolderDetailResponse>> create(
            @Valid @RequestBody CreateFolderRequest request) {
        var created = folderService.create(request);
        return ResponseEntity.ok(new ApiSuccess<>("Folder created", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiSuccess<FolderDetailResponse>> update(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody UpdateFolderRequest request) {
        var updated = folderService.update(id, request);
        return ResponseEntity.ok(new ApiSuccess<>("Folder updated", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiSuccess<Void>> delete(@PathVariable @Min(1) Long id) {
        folderService.delete(id);
        return ResponseEntity.ok(new ApiSuccess<>("Folder deleted", null));
    }
}