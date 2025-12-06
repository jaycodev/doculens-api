package com.doculens.sync.service;

import com.doculens.sync.dto.request.SyncEventRequest;
import com.doculens.sync.dto.response.SyncEventResponse;
import com.doculens.sync.model.SyncEvent;
import com.doculens.sync.repository.SyncEventRepository;
import com.doculens.shared.exception.ResourceNotFoundException;
import com.doculens.user.model.User;
import com.doculens.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SyncEventService {

    private final SyncEventRepository syncEventRepository;
    private final UserRepository userRepository;

    public List<SyncEventResponse> getChanges(Long userId, LocalDateTime lastSync) {
        return syncEventRepository.findChanges(userId, lastSync);
    }

    @Transactional
    public SyncEventResponse log(SyncEventRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SyncEvent event = new SyncEvent();
        event.setUser(user);
        event.setEntityType(request.entityType());
        event.setEntityId(request.entityId());
        event.setOperation(request.operation());

        SyncEvent saved = syncEventRepository.save(event);

        return new SyncEventResponse(saved.getId(),
                saved.getEntityType(),
                saved.getEntityId(),
                saved.getOperation(),
                saved.getTimestamp());
    }
}
