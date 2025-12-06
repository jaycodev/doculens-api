package com.doculens.document.repository;

import com.doculens.document.dto.response.DocumentDetailResponse;
import com.doculens.document.dto.response.DocumentListResponse;
import com.doculens.document.model.Document;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends CrudRepository<Document, Long> {

    @Query("""
            SELECT d.id, d.title, d.folder.id, d.mimeType, d.createdAt
            FROM Document d
            WHERE d.user.id = :userId AND (:folderId IS NULL OR d.folder.id = :folderId)
            ORDER BY d.id DESC
            """)
    List<DocumentListResponse> findList(Long userId, Long folderId);

    @Query("""
            SELECT d.id, d.title, d.fileUrl, d.extractedFields,
                   d.originalFilename, d.mimeType,
                   d.folder.id, d.user.id,
                   (SELECT t.id FROM d.tags t),
                   d.createdAt
            FROM Document d
            WHERE d.id = :id
            """)
    Optional<DocumentDetailResponse> findDetailById(Long id);
}