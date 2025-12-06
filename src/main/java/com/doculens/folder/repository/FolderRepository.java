package com.doculens.folder.repository;

import com.doculens.folder.dto.response.FolderDetailResponse;
import com.doculens.folder.dto.response.FolderListResponse;
import com.doculens.folder.model.Folder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends CrudRepository<Folder, Long> {

    @Query("""
            SELECT
                f.id AS id,
                f.name AS name,
                f.parent.id AS parentId,
                f.createdAt AS createdAt
            FROM Folder f
            WHERE f.user.id = :userId
              AND (:parentId IS NULL OR f.parent.id = :parentId)
            ORDER BY f.id DESC
            """)
    List<FolderListResponse> findList(Long userId, Long parentId);

    @Query("""
            SELECT
                f.id AS id,
                f.name AS name,
                f.parent.id AS parentId,
                f.user.id AS userId,
                f.createdAt AS createdAt
            FROM Folder f
            WHERE f.id = :id
            """)
    Optional<FolderDetailResponse> findDetailById(Long id);
}
