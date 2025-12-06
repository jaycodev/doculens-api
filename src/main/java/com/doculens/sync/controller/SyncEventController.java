package com.doculens.sync.controller;

import com.doculens.shared.api.ApiSuccess;
import com.doculens.sync.dto.request.SyncEventRequest;
import com.doculens.sync.dto.response.SyncEventResponse;
import com.doculens.sync.service.SyncEventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sync")
@RequiredArgsConstructor
@Validated
@Tag(name = "Sync", description = "Operations related to synchronization events")
public class SyncEventController {

    private final SyncEventService syncEventService;

    @GetMapping
    @Operation(summary = "List sync events by user")
    public ResponseEntity<ApiSuccess<List<SyncEventResponse>>> list(
            @RequestParam Long userId,
            @RequestParam String lastSync) {

        LocalDateTime t = LocalDateTime.parse(lastSync);
        var list = syncEventService.getChanges(userId, t);

        return ResponseEntity.ok(new ApiSuccess<>("Sync events listed", list));
    }

    @PostMapping
    @Operation(summary = "Register a new sync event")
    public ResponseEntity<ApiSuccess<SyncEventResponse>> create(
            @Valid @RequestBody SyncEventRequest request) {

        var created = syncEventService.log(request);

        return ResponseEntity.ok(new ApiSuccess<>("Sync event logged", created));
    }
}