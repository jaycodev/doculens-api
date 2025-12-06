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
            SELECT
                d.id AS id,
                d.title AS title,
                d.folder.id AS folderId,
                d.mimeType AS mimeType,
                d.createdAt AS createdAt

            FROM Document d
                WHERE d.user.id = :userId
                    AND (:folderId IS NULL OR d.folder.id = :folderId)
                ORDER BY d.id DESC
            """)
    List<DocumentListResponse> findList(Long userId, Long folderId);

    @Query("""
                SELECT
                    d.id AS id,
                    d.title AS title,
                    d.fileUrl AS fileUrl,
                    d.extractedFields AS extractedFields,
                    d.originalFilename AS originalFilename,
                    d.mimeType AS mimeType,
                    d.folder.id AS folderId,
                    d.user.id AS userId,
                    d.createdAt AS createdAt
                FROM Document d
                WHERE d.id = :id
            """)
    Optional<DocumentDetailResponse> findDetailById(Long id);

    @Query("""
                SELECT DISTINCT d
                FROM Document d
                LEFT JOIN FETCH d.tags
                WHERE d.id = :id
            """)
    Optional<Document> findFullById(Long id);
}