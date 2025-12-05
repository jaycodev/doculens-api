package com.doculens.tag.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.doculens.tag.dto.request.CreateTagRequest;
import com.doculens.tag.dto.request.UpdateTagRequest;
import com.doculens.tag.dto.response.TagDetailResponse;
import com.doculens.tag.dto.response.TagListResponse;
import com.doculens.tag.model.Tag;
import com.doculens.tag.repository.TagRepository;
import com.doculens.user.model.User;
import com.doculens.user.repository.UserRepository;
import com.doculens.shared.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class TagService {
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public List<TagListResponse> getList(Long userId) {
        return tagRepository.findList(userId);
    }

    public TagDetailResponse getDetailById(Long id) {
        return tagRepository.findDetailById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with ID: " + id));
    }

    @Transactional
    public TagListResponse create(CreateTagRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.userId()));

        Tag tag = new Tag();
        tag.setUser(user);
        tag.setName(request.name());

        Tag saved = tagRepository.save(tag);
        return toListResponse(saved);
    }

    @Transactional
    public TagListResponse update(Long id, UpdateTagRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with ID: " + id));

        tag.setName(request.name());

        Tag updated = tagRepository.save(tag);
        return toListResponse(updated);
    }

    private TagListResponse toListResponse(Tag tag) {
        return new TagListResponse(
                tag.getId(),
                tag.getName(),
                tag.getUser().getId(),
                tag.getCreatedAt());
    }
}
