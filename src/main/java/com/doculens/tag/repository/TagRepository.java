package com.doculens.tag.repository;

import com.doculens.tag.dto.response.TagDetailResponse;
import com.doculens.tag.dto.response.TagListResponse;
import com.doculens.tag.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends CrudRepository<Tag, Long> {
    @Query("""
        SELECT 
            t.id AS id,
            t.name AS name,
            t.user.id AS userId,
            t.createdAt AS createdAt
        FROM Tag t
        WHERE t.user.id = :userId
        ORDER BY t.id DESC
        """)
    List<TagListResponse> findList(Long userId);

    @Query("""
        SELECT 
            t.id AS id,
            t.name AS name,
            t.user.id AS userId,
            t.createdAt AS createdAt
        FROM Tag t
        WHERE t.id = :id
        """)
    Optional<TagDetailResponse> findDetailById(Long id);
}
