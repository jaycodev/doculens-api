package com.doculens.sync.repository;

import com.doculens.sync.dto.response.SyncEventResponse;
import com.doculens.sync.model.SyncEvent;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SyncEventRepository extends CrudRepository<SyncEvent, Long> {

    @Query("""
            SELECT s.id, s.entityType, s.entityId, s.operation, s.timestamp
            FROM SyncEvent s
            WHERE s.user.id = :userId AND s.timestamp > :lastSync
            ORDER BY s.timestamp ASC
            """)
    List<SyncEventResponse> findChanges(Long userId, LocalDateTime lastSync);
}