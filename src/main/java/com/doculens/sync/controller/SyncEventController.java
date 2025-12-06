package com.doculens.sync.controller;

import com.doculens.shared.api.ApiSuccess;
import com.doculens.sync.dto.request.SyncEventRequest;
import com.doculens.sync.dto.response.SyncEventResponse;
import com.doculens.sync.service.SyncEventService;

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
public class SyncEventController {

    private final SyncEventService syncEventService;

    @GetMapping
    public ResponseEntity<ApiSuccess<List<SyncEventResponse>>> list(
            @RequestParam Long userId,
            @RequestParam String lastSync) {

        LocalDateTime t = LocalDateTime.parse(lastSync);
        var list = syncEventService.getChanges(userId, t);

        return ResponseEntity.ok(new ApiSuccess<>("Sync events listed", list));
    }

    @PostMapping
    public ResponseEntity<ApiSuccess<SyncEventResponse>> create(
            @Valid @RequestBody SyncEventRequest request) {

        var created = syncEventService.log(request);

        return ResponseEntity.ok(new ApiSuccess<>("Sync event logged", created));
    }
}