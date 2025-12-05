package com.doculens.user.repository;

import com.doculens.user.dto.response.UserDetailResponse;
import com.doculens.user.dto.response.UserListResponse;
import com.doculens.user.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    @Query("""
        SELECT 
            u.id AS id,
            u.email AS email,
            u.role AS role,
            u.createdAt AS createdAt
        FROM User u
        ORDER BY u.id DESC
        """)
    List<UserListResponse> findList();

    @Query("""
        SELECT 
            u.id AS id,
            u.email AS email,
            u.role AS role,
            u.createdAt AS createdAt
        FROM User u
        WHERE u.id = :id
        """)
    Optional<UserDetailResponse> findDetailById(Long id);

    Optional<User> findByEmail(String email);
}
