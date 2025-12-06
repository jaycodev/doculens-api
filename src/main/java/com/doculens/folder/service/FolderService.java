package com.doculens.folder.service;

import com.doculens.folder.dto.request.CreateFolderRequest;
import com.doculens.folder.dto.request.UpdateFolderRequest;
import com.doculens.folder.dto.response.FolderDetailResponse;
import com.doculens.folder.dto.response.FolderListResponse;
import com.doculens.folder.model.Folder;
import com.doculens.folder.repository.FolderRepository;
import com.doculens.shared.exception.ResourceNotFoundException;
import com.doculens.user.model.User;
import com.doculens.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    public List<FolderListResponse> getList(Long userId, Long parentId) {
        return folderRepository.findList(userId, parentId);
    }

    public FolderDetailResponse getDetailById(Long id) {
        return folderRepository.findDetailById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found: " + id));
    }

    @Transactional
    public FolderDetailResponse create(CreateFolderRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.userId()));

        Folder folder = new Folder();
        folder.setName(request.name());
        folder.setUser(user);

        if (request.parentId() != null) {
            Folder parent = folderRepository.findById(request.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent folder not found: " + request.parentId()));
            folder.setParent(parent);
        }

        Folder saved = folderRepository.save(folder);
        return new FolderDetailResponse(
                saved.getId(), saved.getName(),
                saved.getParent() != null ? saved.getParent().getId() : null,
                saved.getUser().getId(),
                saved.getCreatedAt());
    }

    @Transactional
    public FolderDetailResponse update(Long id, UpdateFolderRequest request) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not found: " + id));

        folder.setName(request.name());

        Folder updated = folderRepository.save(folder);

        return new FolderDetailResponse(
                updated.getId(),
                updated.getName(),
                updated.getParent() != null ? updated.getParent().getId() : null,
                updated.getUser().getId(),
                updated.getCreatedAt());
    }

    @Transactional
    public void delete(Long id) {
        if (!folderRepository.existsById(id))
            throw new ResourceNotFoundException("Folder not found: " + id);

        folderRepository.deleteById(id);
    }
}